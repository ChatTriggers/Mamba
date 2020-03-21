package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.ast.nodes.Node

abstract class ExpressionNode(children: List<Node>) : Node(children) {
    constructor(child: Node) : this(listOf(child))

    constructor() : this(emptyList())
}
