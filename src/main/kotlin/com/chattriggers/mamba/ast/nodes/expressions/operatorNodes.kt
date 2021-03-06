package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.toValue

class OrExpresionNode(
    lineNumber: Int,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(lineNumber, listOf(left, right)) {
    override fun execute(ctx: ThreadContext): VObject {
        val leftValue = left.execute(ctx).ifException { return it }
        val rightValue = right.execute(ctx).ifException { return it }

        return (ctx.runtime.toBoolean(leftValue) or ctx.runtime.toBoolean(rightValue)).toValue()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class AndExpressionNode(
    lineNumber: Int,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(lineNumber, listOf(left, right)) {
    override fun execute(ctx: ThreadContext): VObject {
        val leftValue = left.execute(ctx).ifException { return it }
        val rightValue = right.execute(ctx).ifException { return it }

        return (ctx.runtime.toBoolean(leftValue) and ctx.runtime.toBoolean(rightValue)).toValue()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class NotExpressionNode(lineNumber: Int, private val child: ExpressionNode) : ExpressionNode(lineNumber, child) {
    override fun execute(ctx: ThreadContext): VObject {
        val childValue = child.execute(ctx).ifException { return it }
        return ctx.runtime.toBoolean(childValue).not().toValue()
    }
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}

class ComparisonNode(
    lineNumber: Int,
    private val op: ComparisonOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(lineNumber, listOf(left, right)) {
    override fun execute(ctx: ThreadContext): VObject {
        val rt = ctx.runtime
        val leftValue = left.execute(ctx).ifException { return it }
        val rightValue = right.execute(ctx).ifException { return it }

        return when (op) {
            ComparisonOperator.LT -> rt.valueCompare("__lt__", leftValue, rightValue)
            ComparisonOperator.GT -> rt.valueCompare("__gt__", leftValue, rightValue)
            ComparisonOperator.EQ -> rt.valueCompare("__eq__", leftValue, rightValue)
            ComparisonOperator.NOT_EQ -> rt.valueCompare("__ne__", leftValue, rightValue)
            ComparisonOperator.GTE -> rt.valueCompare("__ge__", leftValue, rightValue)
            ComparisonOperator.LTE -> rt.valueCompare("__le__", leftValue, rightValue)
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
    lineNumber: Int,
    private val op: ArithmeticOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(lineNumber, listOf(left, right)) {
    override fun execute(ctx: ThreadContext): VObject {
        val rt = ctx.runtime
        val leftValue = left.execute(ctx).ifException { return it }
        val rightValue = right.execute(ctx).ifException { return it }

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
    lineNumber: Int,
    private val op: UnaryOperator,
    private val child: ExpressionNode
) : ExpressionNode(lineNumber, child) {
    override fun execute(ctx: ThreadContext): VObject {
        val value = child.execute(ctx).ifException { return it }

        return when (op) {
            UnaryOperator.NEG -> ctx.runtime.callProp(value, "__neg__")
            UnaryOperator.POS -> ctx.runtime.callProp(value, "__pos__")
            UnaryOperator.INVERT -> ctx.runtime.callProp(value, "__invert__")
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        printIndent(indent + 1)
        println(op.symbol)
        child.print(indent + 1)
    }
}
