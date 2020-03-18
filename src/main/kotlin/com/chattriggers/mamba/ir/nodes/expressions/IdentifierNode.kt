package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.Value

class IdentifierNode(val identifier: String) : ExpressionNode() {
    override fun execute(interp: Interpreter): Value {
        for (scope in interp.scopeStack) {
            val result = scope[identifier]
            if (result != null)
                return result
        }

        TODO("throw NameError")
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
