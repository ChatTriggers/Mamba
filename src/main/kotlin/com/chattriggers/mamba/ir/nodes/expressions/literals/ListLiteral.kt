package com.chattriggers.mamba.ir.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

class ListLiteral(private val elements: List<ExpressionNode>) : ExpressionNode(elements) {
    override fun execute(interp: Interpreter): VObject {
        return VList(elements.map { it.execute(interp) }.toMutableList())
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}