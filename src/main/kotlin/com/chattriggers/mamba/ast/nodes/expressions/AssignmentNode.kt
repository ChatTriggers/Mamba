package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

class AssignmentNode(
    lineNumber: Int,
    private val identifier: IdentifierNode,
    private val expr: ExpressionNode
) : ExpressionNode(lineNumber, listOf(identifier, expr)) {
    override fun execute(interp: Interpreter): VObject {
        val value = expr.execute(interp)
        interp.getScope()[identifier.identifier] = value
        return value
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        identifier.print(indent + 1)
        expr.print(indent + 1)
    }
}
