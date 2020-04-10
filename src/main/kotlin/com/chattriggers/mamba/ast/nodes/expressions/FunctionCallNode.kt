package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VObject

data class ArgumentNode(
    val value: ExpressionNode,
    val name: String?,
    val spread: Boolean,
    val kwSpread: Boolean
) {

}

data class Argument(
    val value: Value,
    val name: String?,
    val spread: Boolean,
    val kwSpread: Boolean
)

class FunctionCallNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val args: List<ArgumentNode>
) : ExpressionNode(lineNumber, listOf(target) + args.map { it.value }) {
    override fun execute(ctx: ThreadContext): VObject {
        val targetValue = target.execute(ctx)
        val mappedArgs = mutableListOf<Argument>()

        for (arg in args) {
            val value = arg.value.execute(ctx)
            if (value is VExceptionWrapper) return value
            mappedArgs.add(Argument(value, arg.name, arg.spread, arg.kwSpread))
        }

        return ctx.runtime.call(targetValue, mappedArgs)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        // TODO
        // args.forEach { it.print(indent + 1) }
    }
}
