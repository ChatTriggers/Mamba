package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode

enum class IfConditionalNodeType {
    IF,
    ELIF
}

data class IfConditionalNode(
    val type: IfConditionalNodeType,
    val condition: ExpressionNode,
    val body: List<StatementNode>
)

class IfStatementNode(
    lineNumber: Int,
    private val ifBlock: IfConditionalNode,
    private val elifBlocks: List<IfConditionalNode> = emptyList(),
    private val elseBlock: List<StatementNode>
) : StatementNode(lineNumber) {
    init {
        val children = mutableListOf<Node>(ifBlock.condition)
        children.addAll(ifBlock.body.toMutableList())

        for (elifBlock in elifBlocks) {
            children.add(elifBlock.condition)
            children.addAll(elifBlock.body)
        }

        children.addAll(elseBlock)

        for (child in children) {
            child.parent = this
        }
    }

    override fun execute(interp: Interpreter): VObject {
        val rt = interp.runtime
        var ifCond = ifBlock.condition.execute(interp)

        if (rt.toBoolean(ifCond)) {
            return executeStatements(interp, ifBlock.body)
        }

        for (elifBlock in elifBlocks) {
            ifCond = elifBlock.condition.execute(interp)
            if (rt.toBoolean(ifCond))
                return executeStatements(interp, elifBlock.body)
        }

        return executeStatements(interp, elseBlock)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        ifBlock.condition.print(indent + 1)
        ifBlock.body.forEach { it.print(indent + 1) }
        elifBlocks.forEach {
            it.condition.print(indent + 1)
            it.body.forEach {
                it.print(indent + 1)
            }
        }
        printIndent(indent + 1)
        println("ElseBlock")
        elseBlock.forEach {
            it.print(indent + 1)
        }
    }
}