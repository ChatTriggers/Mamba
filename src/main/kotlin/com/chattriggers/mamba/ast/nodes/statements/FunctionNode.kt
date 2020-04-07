package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.values.base.VMethod
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType

data class ParameterNode(
    val identifier: IdentifierNode,
    val defaultValue: ExpressionNode? = null
)

class FunctionNode(
    lineNumber: Int,
    private val identifier: IdentifierNode,
    private val parameters: List<ParameterNode>,
    internal val statements: List<StatementNode>
) : StatementNode(lineNumber, listOf(identifier) + statements) {
    fun call(interp: Interpreter, args: List<VObject>): VObject {
        val requiredArgs = parameters.indexOfFirst { it.defaultValue != null }.let {
            if (it == -1) args.size else it
        }

        if (args.size < requiredArgs)
            notImplemented()

        try {
            val scope = interp.runtime.construct(VObjectType)

            parameters.forEachIndexed { index, node ->
                val value = if (index < args.size) args[index] else node.defaultValue!!.execute(interp)
                scope.putSlot(node.identifier.identifier, value)
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
        scope.putSlot(identifier.identifier, VMethod(this))
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
