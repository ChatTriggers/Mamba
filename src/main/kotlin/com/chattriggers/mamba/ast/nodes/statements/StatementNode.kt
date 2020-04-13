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
        return executeStatements(ctx, children, returnValue = true)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        children.forEach { it.print(indent + 1) }
    }

    companion object {
        fun executeStatements(ctx: ThreadContext, statements: List<Node>, returnValue: Boolean = false): VObject {
            var lastReturned: VObject = VNone

            for (statement in statements) {
                lastReturned = statement.execute(ctx)
                if (lastReturned is VFlowWrapper || lastReturned is VBaseException)
                    return lastReturned
            }

            if (returnValue)
                return lastReturned
            return VNone
        }
    }
}
