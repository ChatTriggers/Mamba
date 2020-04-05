package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VFlowWrapper
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.Node

open class StatementNode(lineNumber: Int, children: List<Node>): Node(lineNumber, children) {
    constructor(lineNumber: Int, child: Node) : this(lineNumber, listOf(child))

    constructor(lineNumber: Int) : this(lineNumber, emptyList())

    override fun execute(interp: Interpreter): VObject {
        children.forEach { it.execute(interp) }
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        children.forEach { it.print(indent + 1) }
    }

    companion object {
        fun executeStatements(interp: Interpreter, statements: List<StatementNode>): VObject {
            for (statement in statements) {
                val returned = statement.execute(interp)
                if (returned is VFlowWrapper)
                    return returned
            }

            return VNone
        }
    }
}
