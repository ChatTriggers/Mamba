package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.numbers.VComplexType
import com.chattriggers.mamba.core.values.numbers.VFloatType
import com.chattriggers.mamba.core.values.numbers.toValue

class IntegerLiteral(lineNumber: Int, private val num: Int) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class FloatLiteral(lineNumber: Int, private val num: Double) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext) = ctx.runtime.construct(VFloatType, listOf(num))

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class ComplexLiteral(lineNumber: Int, private val imag: Double) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext) = ctx.runtime.construct(VComplexType, listOf(0.0, imag))

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$imag\"")
    }
}
