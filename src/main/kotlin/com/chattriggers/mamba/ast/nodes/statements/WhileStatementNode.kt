package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext

class WhileStatementNode(
    lineNumber: Int,
    private val condition: ExpressionNode,
    private val body: List<StatementNode>,
    private val elseBlock: List<StatementNode>
) : StatementNode(lineNumber, listOf(condition) + body + elseBlock) {
    override fun execute(ctx: ThreadContext): VObject {
        var broke = false

        loop@
        while (ctx.runtime.toBoolean(condition.execute(ctx))) {
            // We don't really need to check for VContinueWrapper
            // here, since it's already the last statement
            if (executeStatements(ctx, body) is VBreakWrapper) {
                broke = true
                break@loop
            }
        }

        if (!broke)
            executeStatements(ctx, elseBlock)

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        condition.print(indent + 1)
        body.forEach { it.print(indent + 2) }
        printIndent(indent + 1)
        println("Else Block")
        elseBlock.forEach { it.print(indent + 2) }
    }
}