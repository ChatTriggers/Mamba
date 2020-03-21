package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode

class TupleLiteral(private val elements: List<ExpressionNode>) : ExpressionNode(elements) {
    override fun execute(interp: Interpreter) = when (elements.size) {
        0 -> VTuple.EMPTY_TUPLE
        else -> VTuple(elements.map { it.execute(interp) })
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        elements.forEach { it.print(indent + 1) }
    }
}