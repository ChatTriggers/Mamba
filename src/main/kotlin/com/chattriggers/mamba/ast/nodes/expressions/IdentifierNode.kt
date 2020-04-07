package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VNameError
import com.chattriggers.mamba.core.values.toValue

class IdentifierNode(lineNumber: Int, val identifier: String) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject {
        for (scope in interp.scopeStack) {
            if (scope.containsSlot(identifier)) {
                return scope.getValue(identifier)
            }
        }

        interp.throwException<VNameError>(lineNumber, "name '$identifier' is not defined".toValue())
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
