package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.collections.VStringType
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject

class StringLiteral(lineNumber: Int, private val string: String) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject {
        return ctx.runtime.construct(VStringType, listOf(
            Wrapper(string)
        ))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" $string")
    }
}