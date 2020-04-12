package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.collections.VListType

class ListLiteral(lineNumber: Int, private val elements: List<ExpressionNode>) : ExpressionNode(lineNumber, elements) {
    override fun execute(ctx: ThreadContext): VObject {
        val list = mutableListOf<VObject>()

        for (node in elements) {
            val value = node.execute(ctx).ifException { return it }
            list.add(value)
        }

        return ctx.runtime.construct(VListType, listOf(list))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}