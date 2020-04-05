package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.exceptions.notImplemented

class IdentifierNode(lineNumber: Int, val identifier: String) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject {
        for (scope in interp.scopeStacks) {
            val prop = scope.getOrNull(identifier)
            if (prop != null)
                return prop
        }

        notImplemented("throw NameError")
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
