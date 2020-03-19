package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.toValue

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
    override fun execute(interp: Interpreter): Value {
        val rt = interp.runtime
        val leftValue = left.execute(interp)
        val rightValue = right.execute(interp)

        return when (op) {
            ComparisonOperator.LT -> rt.valueCompare("__lt__", leftValue, rightValue)
            ComparisonOperator.GT -> rt.valueCompare("__gt__", leftValue, rightValue)
            ComparisonOperator.EQ -> rt.valueCompare("__eq__", leftValue, rightValue)
            ComparisonOperator.NOT_EQ -> rt.valueCompare("__ne__", leftValue, rightValue)
            ComparisonOperator.GTE -> rt.valueCompare("__gte__", leftValue, rightValue)
            ComparisonOperator.LTE -> rt.valueCompare("__lte__", leftValue, rightValue)
            ComparisonOperator.DIAMOND -> TODO() // >:(
            ComparisonOperator.IN -> rt.toBoolean(rt.valueCompare("__contains__", leftValue, rightValue)).toValue()
            ComparisonOperator.NOT_IN -> (!rt.toBoolean(rt.valueCompare("__contains__", leftValue, rightValue))).toValue()
            ComparisonOperator.IS -> (leftValue == rightValue).toValue()
            ComparisonOperator.IS_NOT -> (leftValue != rightValue).toValue()
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        printIndent(indent + 1)
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
        val rt = interp.runtime
        val leftValue = left.execute(interp)
        val rightValue = right.execute(interp)

        return when (op) {
            ArithmeticOperator.ADD -> rt.valueArithmetic("__add__", "__radd__", leftValue, rightValue)
            ArithmeticOperator.SUBTRACT -> rt.valueArithmetic("__sub__", "__rsub__", leftValue, rightValue)
            ArithmeticOperator.MULTIPLY -> rt.valueArithmetic("__mul__", "__rmul__", leftValue, rightValue)
            ArithmeticOperator.DIVIDE -> rt.valueArithmetic("__truediv__", "__rtruediv__", leftValue, rightValue)
            ArithmeticOperator.INT_DIVIDE -> rt.valueArithmetic("__floordiv__", "__rfloordiv__", leftValue, rightValue)
            ArithmeticOperator.MODULUS -> rt.valueArithmetic("__mod__", "__rmod__", leftValue, rightValue)
            ArithmeticOperator.POWER -> rt.valueArithmetic("__pow__", "__rpow__", leftValue, rightValue)
            ArithmeticOperator.BIT_AND -> rt.valueArithmetic("__and__", "__rand__", leftValue, rightValue)
            ArithmeticOperator.BIT_XOR -> rt.valueArithmetic("__xor__", "__rxor__", leftValue, rightValue)
            ArithmeticOperator.BIT_OR -> rt.valueArithmetic("__or__", "__ror__", leftValue, rightValue)
            ArithmeticOperator.SHIFT_LEFT -> rt.valueArithmetic("__lshift__", "__rlshift__", leftValue, rightValue)
            ArithmeticOperator.SHIFT_RIGHT -> rt.valueArithmetic("__rshift__", "__rrshift__", leftValue, rightValue)
        }
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
