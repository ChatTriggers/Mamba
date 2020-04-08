package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.collections.VListType

class ListLiteral(lineNumber: Int, private val elements: List<ExpressionNode>) : ExpressionNode(lineNumber, elements) {
    override fun execute(ctx: ThreadContext): VObject {
        return ctx.runtime.construct(VListType, listOf(
            elements.map { it.execute(ctx) }.toMutableList()
        ))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}