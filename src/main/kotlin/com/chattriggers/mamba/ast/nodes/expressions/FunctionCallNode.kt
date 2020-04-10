package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.CallFrame
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
        val mappedArgs = args.map { it.execute(ctx) }

        val interp = ctx.interp

        interp.callStack.push(CallFrame(interp.fileName, interp.sourceStack.peek(), lineNumber))
        interp.sourceStack.push(ctx.runtime.getName(target))

        val v = ctx.runtime.call(targetValue, mappedArgs)
        interp.callStack.pop()

        if (v is VExceptionWrapper) {
            interp.exceptionStack.push(interp.callStack.pop())
        }

        return v
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}
