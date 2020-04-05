package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.functions.VFunctionWrapper
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.values.exceptions.notImplemented

data class ParameterNode(
    val identifier: IdentifierNode,
    val defaultValue: ExpressionNode? = null
)

data class FunctionNode(
    private val identifier: IdentifierNode,
    private val parameters: List<ParameterNode>,
    internal val statements: List<StatementNode>
) : StatementNode(listOf(identifier) + statements), ICallable {
    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        val requiredArgs = parameters.indexOfFirst { it.defaultValue != null }.let {
            if (it == -1) args.size else it
        }

        if (args.size < requiredArgs)
            notImplemented()

        try {
            val scope = VObject()

            parameters.forEachIndexed { index, node ->
                val value = if (index < args.size) args[index] else node.defaultValue!!.execute(interp)
                scope[node.identifier.identifier] = value
            }

            interp.pushScope(scope)

            return when (val returned = executeStatements(interp, statements)) {
                is VReturnWrapper -> returned.wrapped
                is VFlowWrapper -> notImplemented() // Should have been handled by an enclosing node
                else -> VNone
            }
        } finally {
            interp.popScope()
        }
    }

    override fun execute(interp: Interpreter): VObject {
        val scope = interp.getScope()
        scope[identifier.identifier] = VFunctionWrapper(identifier.identifier, this)
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
