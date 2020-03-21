package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VReturnWrapper
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

class ReturnNode(private val child: ExpressionNode) : StatementNode(child) {
    override fun execute(interp: Interpreter): VObject {
        return VReturnWrapper(child.execute(interp))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}