package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VContinueWrapper

class ContinueNode(lineNumber: Int) : StatementNode(lineNumber) {
    override fun execute(interp: Interpreter) = VContinueWrapper

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}