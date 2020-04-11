package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.unwrap



class MemberAccessNode(
    lineNumber: Int,
    val target: ExpressionNode,
    val members: List<ExpressionNode>)
    : ExpressionNode(lineNumber, listOf(target) + members) {
    override fun execute(ctx: ThreadContext): VObject {
        // Note: Currently, members is guaranteed to be a singleton list
        // containing only a regular VObject (i.e. no slices)

        val value = target.execute(ctx).ifException { return it }
        val member = members[0].execute(ctx).ifException { return it }

        return ctx.runtime.callProp(value, "__getitem__", listOf(member))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        members.forEach { it.print(indent + 1) }
    }
}

class DotAccessNode(
    lineNumber: Int,
    val target: ExpressionNode,
    internal val property: IdentifierNode
) : ExpressionNode(lineNumber, listOf(target, property)) {
    override fun execute(ctx: ThreadContext): VObject {
        val obj = target.execute(ctx).ifException { return it }

        return if (obj.containsSlot(property.identifier)) {
            obj.getValue(property.identifier).unwrap()
        } else VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        property.print(indent + 1)
    }
}
