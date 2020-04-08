package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VBreakWrapper

class BreakNode(lineNumber: Int) : StatementNode(lineNumber) {
    override fun execute(ctx: ThreadContext) = VBreakWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}