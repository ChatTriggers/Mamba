package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.statements.StatementNode

class ScriptNode(private val statements: List<StatementNode>) : Node(statements) {
    override fun execute(interp: Interpreter): VObject {
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