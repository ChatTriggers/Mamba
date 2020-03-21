package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.numbers.VComplex
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode

class IntegerLiteral(private val num: Int) : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class FloatLiteral(private val num: Double) : ExpressionNode() {
    override fun execute(interp: Interpreter) = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class ComplexLiteral(private val imag: Double) : ExpressionNode() {
    override fun execute(interp: Interpreter) = VComplex(0.0, imag)

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$imag\"")
    }
}
