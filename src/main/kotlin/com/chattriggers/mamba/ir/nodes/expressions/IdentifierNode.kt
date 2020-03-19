package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

class IdentifierNode(val identifier: String) : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject {
        for (scope in interp.scopeStack) {
            if (scope.has(identifier)) {
                return scope.lookup(identifier)!!
            }
        }

        TODO("throw NameError")
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
