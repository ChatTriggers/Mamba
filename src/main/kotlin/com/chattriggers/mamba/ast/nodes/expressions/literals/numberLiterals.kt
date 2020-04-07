package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.numbers.VComplex
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode

class IntegerLiteral(lineNumber: Int, private val num: Int) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class FloatLiteral(lineNumber: Int, private val num: Double) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter) = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class ComplexLiteral(lineNumber: Int, private val imag: Double) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter) = VComplex(0.0, imag)

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$imag\"")
    }
}
