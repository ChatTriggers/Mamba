package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.GlobalScope
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.unwrap

class AssignmentNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val expr: ExpressionNode
) : ExpressionNode(lineNumber, listOf(target, expr)) {
    override fun execute(ctx: ThreadContext): VObject {
        val value = expr.execute(ctx)
        if (value is VExceptionWrapper) return value

        when (target) {
            is IdentifierNode -> {
                val currScope = ctx.interp.scopes.currScope
                val ident = target.identifier

                if (ident in currScope.globals) {
                    GlobalScope.putSlot(ident, value)
                } else if (ident in currScope.nonlocals) {
                    var found = false

                    for (scope in ctx.interp.scopes.scopeStack.reversed().drop(1).dropLast(1)) {
                        if (scope.containsSlot(ident)) {
                            scope.putSlot(ident, value)
                            found = true
                            break
                        }
                    }

                    if (!found)
                        TODO("NameError")
                } else {
                    currScope.putSlot(ident, value)
                }
            }
            is MemberAccessNode -> {
                val targetObj = target.target.execute(ctx)
                if (targetObj is VExceptionWrapper)
                    return targetObj

                val member = target.members[0].execute(ctx)
                if (member is VExceptionWrapper)
                    return member

                ctx.runtime.callProp(targetObj, "__setitem__", listOf(member, value))
            }
            is DotAccessNode -> {
                val targetObj = target.target.execute(ctx)
                if (targetObj is VExceptionWrapper)
                    return targetObj

                val prop = target.property.execute(ctx)
                if (prop is VExceptionWrapper)
                    return prop

                ctx.runtime.callProp(targetObj, "__setattr__", listOf(prop, value))
            }
        }

        return value
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        expr.print(indent + 1)
    }
}
