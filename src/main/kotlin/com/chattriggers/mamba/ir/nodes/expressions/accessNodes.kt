package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VObject

class MemberAccessNode(
    private val target: ExpressionNode,
    private val members: List<ExpressionNode>)
    : ExpressionNode(listOf(target) + members) {
    override fun execute(interp: Interpreter): VObject {
        TODO()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        members.forEach { it.print(indent + 1) }
    }
}

class DotAccessNode(
    private val target: ExpressionNode,
    private val property: IdentifierNode
) : ExpressionNode(listOf(target, property)) {
    override fun execute(interp: Interpreter): VObject {
        return target.execute(interp)[property.identifier] ?: VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        property.print(indent + 1)
    }
}
