package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.Value

class AssignmentNode(
    private val identifier: IdentifierNode,
    private val expr: ExpressionNode
) : ExpressionNode(listOf(identifier, expr)) {
    override fun execute(interp: Interpreter): Value {
        val value = expr.execute(interp)
        return interp.lexicalAssign(identifier.identifier, value)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        identifier.print(indent + 1)
        expr.print(indent + 1)
    }
}
