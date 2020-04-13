package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.ast.nodes.statements.StatementNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject

class ScriptNode(private val statements: List<StatementNode>) : Node(1, statements) {
    override fun execute(ctx: ThreadContext): VObject {
        return StatementNode.executeStatements(ctx, statements, returnValue = true)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        statements.forEach { it.print(indent + 1) }
    }
}