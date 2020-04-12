package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.singletons.VNone

class ForStatementNode(
    lineNumber: Int,
    private val targetNode: ExpressionNode,
    private val iterableNode: ExpressionNode,
    private val body: List<StatementNode>,
    private val elseBlock: List<StatementNode>
) : StatementNode(lineNumber, listOf(targetNode, iterableNode) + body + elseBlock) {
    override fun execute(ctx: ThreadContext): VObject {
        val iterable = iterableNode.execute(ctx).ifException { return it }
        val iterator = ctx.runtime.getIterator(iterable).ifException { return it }

        if (targetNode !is IdentifierNode)
            TODO()

        val targetName = targetNode.identifier

        var didBreak = false

        loop@
        while (true) {
            val nextValue = ctx.runtime.getIteratorNext(iterator)

            when (nextValue) {
                is VStopIteration -> break@loop
                is VBaseException -> return nextValue
            }

            ctx.interp.scopes.currScope.putSlot(targetName, nextValue)

            when (val execResult = executeStatements(ctx, body)) {
                VBreakWrapper -> {
                    didBreak = true
                    break@loop
                }
                is VReturnWrapper -> return execResult
                is VBaseException -> return execResult
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