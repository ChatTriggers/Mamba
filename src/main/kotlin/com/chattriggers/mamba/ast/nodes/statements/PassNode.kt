package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.singletons.VNone

class PassNode(lineNumber: Int) : StatementNode(lineNumber, emptyList()) {
    override fun execute(ctx: ThreadContext) = VNone

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}