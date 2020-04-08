package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext

class ReturnNode(lineNumber: Int, private val child: ExpressionNode) : StatementNode(lineNumber, child) {
    override fun execute(ctx: ThreadContext): VObject {
        return VReturnWrapper(child.execute(ctx))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}