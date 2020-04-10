package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.ast.nodes.statements.StatementNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper

class ScriptNode(private val statements: List<StatementNode>) : Node(1, statements) {
    override fun execute(ctx: ThreadContext): VObject {
        val result = StatementNode.executeStatements(ctx, statements)

        if (result is VExceptionWrapper) {
            println("Exception:")
            println(result.exception)
        }

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        statements.forEach { it.print(indent + 1) }
    }
}