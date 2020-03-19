package com.chattriggers.mamba.ir.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.functions.VFunction
import com.chattriggers.mamba.ir.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.ir.nodes.statements.StatementNode

data class FunctionNode(
    private val identifier: IdentifierNode,
    internal val statements: List<StatementNode>
) : StatementNode(listOf(identifier) + statements), ICallable {
    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        try {
            val scope = VObject()
            // TODO: Populate arguments
            // Accept a list of identifiers in the constructor,
            // and associate each one of those with the corresponding
            // arguments in the scope
            interp.pushScope(scope)

            return when (val returned = executeStatements(interp, statements)) {
                is VReturnWrapper -> returned.wrapped
                is VFlowWrapper -> TODO() // Should have been handled by an enclosing node
                else -> VNone
            }
        } finally {
            interp.popScope()
        }
    }

    override fun execute(interp: Interpreter): VObject {
        val scope = interp.getScope()
        scope[identifier.identifier] =
            VFunction(identifier.identifier, this)
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
