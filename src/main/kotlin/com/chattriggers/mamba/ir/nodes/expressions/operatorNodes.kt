package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.Value

class OrExpresionNode(
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class AndExpressionNode(
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class NotExpressionNode(private val child: ExpressionNode) : ExpressionNode(child) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}

class ComparisonNode(
    private val op: ComparisonOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        printIndent(indent)
        println(op.symbol)
        right.print(indent + 1)
    }
}

class ArithmeticExpressionNode(
    private val op: ArithmeticOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun execute(interp: Interpreter): Value {
        return interp.runtime.add(left.execute(interp), right.execute(interp))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        printIndent(indent + 1)
        println(op.symbol)
        right.print(indent + 1)
    }
}

class UnaryExpressionNode(
    private val op: UnaryOperator,
    private val child: ExpressionNode
) : ExpressionNode(child) {
    override fun execute(interp: Interpreter): Value {
        val value = child.execute(interp)

        return when (op) {
            UnaryOperator.NEG -> value.callFunction(interp, "__neg__")
            UnaryOperator.POS -> value.callFunction(interp, "__pos__")
            UnaryOperator.INVERT -> value.callFunction(interp, "__invert__")
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        printIndent(indent + 1)
        println(op.symbol)
        child.print(indent + 1)
    }
}
