package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VContinueWrapper

class ContinueNode(lineNumber: Int) : StatementNode(lineNumber) {
    override fun execute(ctx: ThreadContext) = VContinueWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}