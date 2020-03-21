package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VBreakWrapper

object BreakNode : StatementNode() {
    override fun execute(interp: Interpreter) = VBreakWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}