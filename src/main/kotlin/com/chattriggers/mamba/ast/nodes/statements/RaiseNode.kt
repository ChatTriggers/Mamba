package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VTypeError

class RaiseNode(
    lineNumber: Int,
    private val exceptionNode: ExpressionNode
) : StatementNode(lineNumber, exceptionNode) {
    override fun execute(ctx: ThreadContext): VObject {
        exceptionNode.execute(ctx).ifException { return it }

        return VTypeError.construct("exceptions must derive from BaseException").apply {
            initializeCallstack(lineNumber)
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}