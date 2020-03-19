package com.chattriggers.mamba.ir.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.statements.StatementNode

class ScriptNode(private val statements: List<StatementNode>) : Node(statements) {
    override fun execute(interp: Interpreter): Value {
        statements.forEach {
            it.execute(interp)
        }

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        statements.forEach { it.print(indent + 1) }
    }
}