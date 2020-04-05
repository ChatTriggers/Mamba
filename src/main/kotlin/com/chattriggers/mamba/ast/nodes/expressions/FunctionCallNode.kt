package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.singletons.VNone

class FunctionCallNode(
    lineNumber: Int,
    private val target: ExpressionNode,
    private val args: List<ExpressionNode>
) : ExpressionNode(lineNumber, listOf(target) + args) {
    override fun execute(interp: Interpreter): VObject {
        val targetValue = target.execute(interp)

        val mappedArgs = args.map { it.execute(interp) }

        return try {
            interp.runtime.call(targetValue, mappedArgs)
        } catch (e: MambaException) {
            interp.pushCallStack(interp.fileName, interp.runtime.getName(target), lineNumber)
            throw e
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}
