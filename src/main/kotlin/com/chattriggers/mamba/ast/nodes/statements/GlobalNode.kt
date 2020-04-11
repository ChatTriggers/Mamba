package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.VNone

class GlobalNode(
    lineNumber: Int,
    private val target: IdentifierNode
) : StatementNode(lineNumber, listOf(target)) {
    override fun execute(ctx: ThreadContext): VObject {
        ctx.interp.scopes.currScope.globals.add(target.identifier)
        return VNone
    }
}
