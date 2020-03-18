package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.ir.nodes.Node

abstract class ExpressionNode(children: List<Node>) : Node(children) {
    constructor(child: Node) : this(listOf(child))

    constructor() : this(emptyList())
}
