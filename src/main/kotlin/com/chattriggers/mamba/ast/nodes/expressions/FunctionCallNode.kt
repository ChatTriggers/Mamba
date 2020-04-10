package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.base.VObject

class FunctionCallNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val args: List<ExpressionNode>
) : ExpressionNode(lineNumber, listOf(target) + args) {
    override fun execute(ctx: ThreadContext): VObject {
        val targetValue = target.execute(ctx)
        val mappedArgs = mutableListOf<VObject>()

        for (arg in args) {
            val value = arg.execute(ctx)
            if (value is VExceptionWrapper) return value
            mappedArgs.add(value)
        }

        return ctx.runtime.call(targetValue, mappedArgs)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}
