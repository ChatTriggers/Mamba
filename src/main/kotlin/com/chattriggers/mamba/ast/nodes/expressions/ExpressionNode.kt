package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.ast.nodes.Node

abstract class ExpressionNode(lineNumber: Int, children: List<Node>) : Node(lineNumber, children) {
    constructor(lineNumber: Int, child: Node) : this(lineNumber, listOf(child))

    constructor(lineNumber: Int) : this(lineNumber, emptyList())
}
