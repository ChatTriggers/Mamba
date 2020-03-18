package com.chattriggers.mamba.ir.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.numbers.toValue

class StringLiteral(private val string: String) : Literal() {
    override fun execute(interp: Interpreter): Value {
        return VString(string)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" $string")
    }
}

// TODO: Number formatting
class NumberLiteral(private val num: Int) : Literal() {
    override fun execute(interp: Interpreter) = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}
