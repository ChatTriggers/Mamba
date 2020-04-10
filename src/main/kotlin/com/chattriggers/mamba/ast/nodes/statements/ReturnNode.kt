package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper

class ReturnNode(lineNumber: Int, private val child: ExpressionNode) : StatementNode(lineNumber, child) {
    override fun execute(ctx: ThreadContext): VObject {
        val value = child.execute(ctx)
        if (value is VExceptionWrapper) return value
        return VReturnWrapper(value)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}