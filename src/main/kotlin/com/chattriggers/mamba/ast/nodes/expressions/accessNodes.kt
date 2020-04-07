package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.notImplemented

class MemberAccessNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val members: List<ExpressionNode>)
    : ExpressionNode(lineNumber, listOf(target) + members) {
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
    lineNumber: Int,
    private val target: ExpressionNode,
    internal val property: IdentifierNode
) : ExpressionNode(lineNumber, listOf(target, property)) {
    override fun execute(interp: Interpreter): VObject {
        val obj = target.execute(interp)
        return if (obj.containsSlot(property.identifier)) {
            obj.getValue(property.identifier)
        } else VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        property.print(indent + 1)
    }
}
