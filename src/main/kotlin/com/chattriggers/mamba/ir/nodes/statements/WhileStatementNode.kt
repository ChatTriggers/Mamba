package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VBreakWrapper
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

class WhileStatementNode(
    private val condition: ExpressionNode,
    private val body: List<StatementNode>,
    private val elseBlock: List<StatementNode>
) : StatementNode(listOf(condition) + body + elseBlock) {
    override fun execute(interp: Interpreter): VObject {
        var broke = false

        loop@
        while (interp.runtime.toBoolean(condition.execute(interp))) {
            // We don't really need to check for VContinueWrapper
            // here, since it's already the last statement
            if (executeStatements(interp, body) is VBreakWrapper) {
                broke = true
                break@loop
            }
        }

        if (!broke)
            executeStatements(interp, elseBlock)

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