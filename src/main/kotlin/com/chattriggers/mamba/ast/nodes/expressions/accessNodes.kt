package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.exceptions.notImplemented

class MemberAccessNode(
    private val target: ExpressionNode,
    private val members: List<ExpressionNode>)
    : ExpressionNode(listOf(target) + members) {
    override fun execute(interp: Interpreter): VObject {
        notImplemented()
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
        return target.execute(interp).getOrNull(property.identifier) ?: VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        property.print(indent + 1)
    }
}
