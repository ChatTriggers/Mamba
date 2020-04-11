package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.toValue

class RaiseNode(
    lineNumber: Int,
    private val exceptionNode: ExpressionNode
) : StatementNode(lineNumber, exceptionNode) {
    override fun execute(ctx: ThreadContext): VObject {
        val exception = exceptionNode.execute(ctx).ifException { return it }

        if (exception !is VBaseException) {
            return VExceptionWrapper(lineNumber, VTypeError.construct("exceptions must derive from BaseException"))
        }

        return VExceptionWrapper(lineNumber, exception)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}