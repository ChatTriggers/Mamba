package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject

class GlobalNode(
    lineNumber: Int,
    val target: IdentifierNode
) : StatementNode(lineNumber, listOf(target)) {
    override fun execute(ctx: ThreadContext): VObject {
        return super.execute(ctx)
    }
}
