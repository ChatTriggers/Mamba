package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.VContinueWrapper
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
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
            TODO()

        val targetName = targetNode.identifier

        val scope = VObject()
        interp.pushScope(scope)

        var didBreak = false

        try {
            while (true) {
                val nextValue = interp.runtime.getIteratorNext(iterator)

                // TODO: This is a placeholder for a StopIteration exception
                if (nextValue is VNotImplemented) {
                    break
                }

                scope[targetName] = nextValue

                when (val execResult = executeStatements(interp, body)) {
                    VBreakWrapper -> didBreak = true
                    is VReturnWrapper -> return execResult
                }
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