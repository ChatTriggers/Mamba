package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.toValue

class RaiseNode(
    lineNumber: Int,
    private val exceptionNode: ExpressionNode
) : StatementNode(lineNumber, exceptionNode) {
    override fun execute(interp: Interpreter): VObject {
        val exception = exceptionNode.execute(interp)

        if (exception !is VBaseException) {
            interp.throwException<VTypeError>(lineNumber, "exceptions must derive from BaseException".toValue())
        }

        interp.throwException(exception, lineNumber)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}