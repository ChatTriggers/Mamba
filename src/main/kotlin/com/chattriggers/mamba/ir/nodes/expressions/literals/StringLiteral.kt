package com.chattriggers.mamba.ir.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VString

class StringLiteral(private val string: String) : Literal() {
    override fun execute(interp: Interpreter): VObject {
        return VString(string)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" $string")
    }
}