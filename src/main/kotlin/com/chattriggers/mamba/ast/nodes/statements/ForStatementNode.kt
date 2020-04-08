package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone

class ForStatementNode(
    lineNumber: Int,
    private val targetNode: ExpressionNode,
    private val iterableNode: ExpressionNode,
    private val body: List<StatementNode>,
    private val elseBlock: List<StatementNode>
) : StatementNode(lineNumber, listOf(targetNode, iterableNode) + body + elseBlock) {
    override fun execute(ctx: ThreadContext): VObject {
        val iterator = ctx.runtime.getIterator(iterableNode.execute(ctx))

        if (targetNode !is IdentifierNode)
            notImplemented()

        val targetName = targetNode.identifier

        var didBreak = false

        try {
            while (true) {
                val nextValue = ctx.runtime.getIterableNext(iterator)

                ctx.interp.getScope().putSlot(targetName, nextValue)

                when (val execResult = executeStatements(ctx, body)) {
                    VBreakWrapper -> didBreak = true
                    is VReturnWrapper -> return execResult
                }
            }
        } catch (e: MambaException) {
            val reason = e.reason

            if (reason !is VStopIteration) {
                throw e
            }
        }

        if (!didBreak && elseBlock.isNotEmpty()) {
            return executeStatements(ctx, elseBlock)
        }

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)

        printIndent(indent + 1)
        print("Iterator: ")
        targetNode.print(0)

        printIndent(indent + 1)
        print("Iterable: ")
        iterableNode.print(0)

        printIndent(indent + 1)
        println("Body: ")
        body.forEach { it.print(indent + 2) }

        if (elseBlock.isNotEmpty()) {
            printIndent(indent + 1)
            println("ElseBlock: ")
            elseBlock.forEach { it.print(indent + 2) }
        }
    }
}