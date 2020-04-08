package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext

class ListLiteral(lineNumber: Int, private val elements: List<ExpressionNode>) : ExpressionNode(lineNumber, elements) {
    override fun execute(ctx: ThreadContext): VObject {
        return VList(elements.map { it.execute(ctx) }.toMutableList())
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}