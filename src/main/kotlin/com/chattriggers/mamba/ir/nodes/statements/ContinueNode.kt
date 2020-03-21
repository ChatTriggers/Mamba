package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VContinueWrapper

object ContinueNode : StatementNode() {
    override fun execute(interp: Interpreter) = VContinueWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}