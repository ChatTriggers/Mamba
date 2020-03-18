package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.Value

class FunctionCallNode(
    private val target: ExpressionNode,
    private val args: List<ExpressionNode>
) : ExpressionNode(listOf(target) + args) {
    override fun execute(interp: Interpreter): Value {
        val targetValue = target.execute(interp)
        var callable: ICallable? = null

        if (targetValue is ICallable)
            callable = targetValue

        if (callable == null)
            TODO()

        return callable.call(interp, args.map { it.execute(interp) })
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}
