package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.Node

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
