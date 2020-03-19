package com.chattriggers.mamba.ir.nodes.statements

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.toValue
import com.chattriggers.mamba.ir.nodes.Node
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode

enum class IfConditionalNodeType {
    IF,
    ELIF,
    ELSE
}

data class IfConditionalNode(
    val type: IfConditionalNodeType,
    val condition: ExpressionNode?,
    val body: List<StatementNode>
) : Node(if (condition == null) emptyList() else listOf(condition) + body) {
    override fun execute(interp: Interpreter): VObject {
        return if (condition == null || interp.runtime.toBoolean(condition.execute(interp))) {
            body.forEach { it.execute(interp) }
            true
        } else {
            false
        }.toValue()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)

        when (type) {
            IfConditionalNodeType.IF -> println(" (if)")
            IfConditionalNodeType.ELIF -> println(" (elif)")
            IfConditionalNodeType.ELSE -> println(" (else)")
        }

        condition?.print(indent + 1)
        body.forEach {
            it.print(indent + 1)
        }
    }
}

class IfStatementNode(
    private val ifBlock: IfConditionalNode,
    private val elifBlocks: List<IfConditionalNode> = emptyList(),
    private val elseBlock: IfConditionalNode? = null
) : StatementNode(listOf(ifBlock) + elifBlocks + if (elseBlock == null) emptyList() else listOf(elseBlock)) {
    override fun execute(interp: Interpreter): VObject {
        interp.pushScope()

        try {
            var result = ifBlock.execute(interp)

            if (interp.runtime.toBoolean(result))
                return VNone

            for (elifBlock in elifBlocks) {
                result = elifBlock.execute(interp)
                if (interp.runtime.toBoolean(result))
                    return VNone
            }

            if (elseBlock != null) {
                result = elseBlock.execute(interp)
                if (interp.runtime.toBoolean(result))
                    return VNone
            }

            return VNone
        } finally {
            interp.popScope()
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        ifBlock.print(indent + 1)
        elifBlocks.forEach {
            it.print(indent + 1)
        }
        elseBlock?.print(indent + 1)
    }
}