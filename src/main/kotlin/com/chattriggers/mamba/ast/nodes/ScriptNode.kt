package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.ast.nodes.statements.StatementNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper

class ScriptNode(private val statements: List<StatementNode>) : Node(1, statements) {
    override fun execute(ctx: ThreadContext): VObject {
        StatementNode.executeStatements(ctx, statements).ifException { ex ->
            println("Traceback (most recent call last):")

            ex.callStack.forEach { cs ->
                println("  File \"${cs.source}\", line ${cs.lineNumber}, in ${cs.name}")
                println("    ${ctx.interp.lines[cs.lineNumber - 1].trim()}")
            }

            println(ex.exception)
        }

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        statements.forEach { it.print(indent + 1) }
    }
}