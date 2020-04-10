package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.base.VObject

class AssignmentNode(
    lineNumber: Int,
    private val identifier: IdentifierNode,
    private val expr: ExpressionNode
) : ExpressionNode(lineNumber, listOf(identifier, expr)) {
    override fun execute(ctx: ThreadContext): VObject {
        val value = expr.execute(ctx)
        if (value is VExceptionWrapper) return value

        ctx.interp.getScope().putSlot(identifier.identifier, value)
        return value
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        identifier.print(indent + 1)
        expr.print(indent + 1)
    }
}
