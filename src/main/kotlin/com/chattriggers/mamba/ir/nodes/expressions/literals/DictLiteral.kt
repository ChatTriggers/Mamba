package com.chattriggers.mamba.ir.nodes.expressions.literals

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.ir.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ir.nodes.expressions.IdentifierNode

class DictLiteral(private val dict: Map<ExpressionNode, ExpressionNode>) : Literal() {
    override fun execute(interp: Interpreter): VObject {
        return VDict(
            dict.mapKeys { (key) ->
                if (key !is IdentifierNode && key !is StringLiteral)
                    TODO()

                (key.execute(interp) as VString).string
            }.mapValues { (_, value) ->
                value.execute(interp)
            }.toMutableMap()
        )
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        dict.forEach { (key, value) ->
            key.print(indent + 1)
            value.print(indent + 2)
        }
    }
}