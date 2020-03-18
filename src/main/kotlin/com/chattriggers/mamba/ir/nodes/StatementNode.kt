package com.chattriggers.mamba.ir.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.ICallable
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

open class StatementNode(children: List<Node>): Node(children) {
    constructor(child: Node) : this(listOf(child))

    constructor() : this(emptyList())

    override fun execute(interp: Interpreter): Value {
        children.forEach { it.execute(interp) }
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        children.forEach { it.print(indent + 1) }
    }
}

class ReturnNode(private val child: ExpressionNode) : StatementNode(child) {
    override fun execute(interp: Interpreter): Value {
        val parentFunc = getParentOfType<ICallable>() ?: TODO("Error")
        parentFunc.returnValue(child.execute(interp))

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}
