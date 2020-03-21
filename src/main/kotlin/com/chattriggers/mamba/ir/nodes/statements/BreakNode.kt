package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.VObject

object BreakNode : StatementNode() {
    override fun execute(interp: Interpreter) = VBreakWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}