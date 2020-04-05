package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.VContinueWrapper
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VNotImplemented

class ForStatementNode(
    private val targetNode: ExpressionNode,
    private val iterableNode: ExpressionNode,
    private val body: List<StatementNode>,
    private val elseBlock: List<StatementNode>
) : StatementNode(listOf(targetNode, iterableNode) + body + elseBlock) {
    override fun execute(interp: Interpreter): VObject {
        val iterator = interp.runtime.getIterator(iterableNode.execute(interp))

        if (targetNode !is IdentifierNode)
            notImplemented()

        val targetName = targetNode.identifier

        val scope = VObject()
        interp.pushScope(scope)

        var didBreak = false

        try {
            while (true) {
                val nextValue = interp.runtime.getIteratorNext(iterator)

                scope[targetName] = nextValue

                when (val execResult = executeStatements(interp, body)) {
                    VBreakWrapper -> didBreak = true
                    is VReturnWrapper -> return execResult
                }
            }
        } catch (e: MambaException) {
            val reason = e.reason

            if (reason !is VStopIteration) {
                throw e
            }
        } finally {
            interp.popScope()
        }

        if (!didBreak && elseBlock.isNotEmpty()) {
            interp.pushScope()
            try {
                return executeStatements(interp, elseBlock)
            } finally {
                interp.popScope()
            }
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