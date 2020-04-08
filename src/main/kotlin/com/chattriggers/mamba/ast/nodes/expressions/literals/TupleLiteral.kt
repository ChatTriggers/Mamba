package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.collections.VTupleType

class TupleLiteral(lineNumber: Int, private val elements: List<ExpressionNode>) : ExpressionNode(lineNumber, elements) {
    override fun execute(ctx: ThreadContext) = when (elements.size) {
        0 -> VTuple.EMPTY_TUPLE
        else -> ctx.runtime.construct(VTupleType, listOf((elements.map { it.execute(ctx) })))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}