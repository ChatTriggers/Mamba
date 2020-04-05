package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode

class StringLiteral(lineNumber: Int, private val string: String) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject {
        return VString(string)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" $string")
    }
}