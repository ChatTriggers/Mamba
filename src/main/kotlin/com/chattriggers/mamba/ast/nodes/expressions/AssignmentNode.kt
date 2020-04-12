package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.GlobalScope
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VSyntaxError

class AssignmentNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val expr: ExpressionNode
) : ExpressionNode(lineNumber, listOf(target, expr)) {
    override fun execute(ctx: ThreadContext): VObject {
        val value = expr.execute(ctx).ifException { return it }

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

                    if (!found) {
                        return VSyntaxError.construct("no binding for nonlocal '$ident' found").apply {
                            initializeCallstack(lineNumber)
                        }
                    }
                } else {
                    currScope.putSlot(ident, value)
                }
            }
            is MemberAccessNode -> {
                val targetObj = target.target.execute(ctx).ifException { return it }
                val member = target.members[0].execute(ctx).ifException { return it }

                ctx.runtime.callProp(targetObj, "__setitem__", listOf(member, value)).ifException {
                    return it
                }
            }
            is DotAccessNode -> {
                val targetObj = target.target.execute(ctx).ifException { return it }
                val prop = target.property.execute(ctx).ifException { return it }

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
