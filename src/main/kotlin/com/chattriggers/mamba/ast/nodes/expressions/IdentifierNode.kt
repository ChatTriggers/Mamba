package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.GlobalScope
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VListType
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.exceptions.VNameError
import com.chattriggers.mamba.core.values.exceptions.VNameErrorType
import com.chattriggers.mamba.core.values.toValue
import com.chattriggers.mamba.core.values.unwrap

class IdentifierNode(lineNumber: Int, val identifier: String) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject {
        val scopes = ctx.interp.scopes
        val currScope = scopes.currScope

        if (identifier in currScope.globals) {
            return GlobalScope.getValue(identifier).unwrap()
        } else {
            for (scope in scopes.scopeStack.reversed()) {
                if (scope.containsSlot(identifier))
                    return scope.getValue(identifier).unwrap()
            }
        }

        return VNameError.construct(identifier).apply {
            initializeCallstack(lineNumber)
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }

    override fun toString() = identifier
}
