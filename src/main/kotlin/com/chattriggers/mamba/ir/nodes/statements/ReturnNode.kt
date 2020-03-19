package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.FunctionNode
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

class ReturnNode(private val child: ExpressionNode) : StatementNode(child) {
    override fun execute(interp: Interpreter): Value {
        val parentFunc = getParentOfType<FunctionNode>() ?: TODO("Error")
        parentFunc.returnValue(child.execute(interp))

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}