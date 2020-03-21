package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

class FunctionCallNode(
    private val target: ExpressionNode,
    private val args: List<ExpressionNode>
) : ExpressionNode(listOf(target) + args) {
    override fun execute(interp: Interpreter): VObject {
        val targetValue = target.execute(interp)
        return interp.runtime.call(targetValue, args.map { it.execute(interp) })
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}
