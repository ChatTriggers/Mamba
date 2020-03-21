package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

class IdentifierNode(val identifier: String) : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject {
        for (scope in interp.scopeStack) {
            val prop = scope.getOrNull(identifier)
            if (prop != null)
                return prop
        }

        TODO("throw NameError")
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
