package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VFunctionType
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
    fun call(ctx: ThreadContext, args: List<VObject>): VObject {
        val requiredArgs = parameters.indexOfFirst { it.defaultValue != null }.let {
            if (it == -1) args.size else it
        }

        if (args.size < requiredArgs)
            TODO()

        try {
            val scope = ctx.runtime.construct(VObjectType)

            parameters.forEachIndexed { index, node ->
                val value = if (index < args.size) args[index] else node.defaultValue!!.execute(ctx)
                scope.putSlot(node.identifier.identifier, value)
            }

            ctx.interp.pushScope(scope)

            return when (val returned = executeStatements(ctx, statements)) {
                is VReturnWrapper -> returned.wrapped
                is VExceptionWrapper -> return ctx.interp.throwException(returned.exception, lineNumber)
                is VFlowWrapper -> TODO() // Should have been handled by an enclosing node
                else -> VNone
            }
        } finally {
            ctx.interp.popScope()
        }
    }

    override fun execute(ctx: ThreadContext): VObject {
        val scope = ctx.interp.getScope()
        scope.putSlot(identifier.identifier, MethodWrapper(identifier.identifier, this))
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
