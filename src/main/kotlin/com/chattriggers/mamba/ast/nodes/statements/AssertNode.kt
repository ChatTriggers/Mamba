package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.exceptions.VAssertionError
import com.chattriggers.mamba.core.values.exceptions.VAssertionErrorType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class AssertNode(
    lineNumber: Int,
    private val condition: ExpressionNode,
    private val message: ExpressionNode?
) : StatementNode(lineNumber, listOf(condition) + if (message == null) emptyList() else listOf(message)) {
    override fun execute(ctx: ThreadContext): VObject {
        val conditionValue = condition.execute(ctx).ifException { return it }
        val isTrue = ctx.runtime.toBoolean(conditionValue)

        if (!isTrue) {
            val messageValue = message?.execute(ctx)?.ifException { return it } ?: "".toValue()
            return VAssertionError.construct(messageValue).apply {
                initializeCallstack(lineNumber)
            }
        }

        return VNone
    }
}