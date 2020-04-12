package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VFlowWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.singletons.VNone

open class StatementNode(lineNumber: Int, children: List<Node>): Node(lineNumber, children) {
    constructor(lineNumber: Int, child: Node) : this(lineNumber, listOf(child))

    constructor(lineNumber: Int) : this(lineNumber, emptyList())

    override fun execute(ctx: ThreadContext): VObject {
        return executeStatements(ctx, children)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        children.forEach { it.print(indent + 1) }
    }

    companion object {
        fun executeStatements(ctx: ThreadContext, statements: List<Node>): VObject {
            for (statement in statements) {
                val returned = statement.execute(ctx)
                if (returned is VFlowWrapper || returned is VBaseException)
                    return returned
            }

            return VNone
        }
    }
}
