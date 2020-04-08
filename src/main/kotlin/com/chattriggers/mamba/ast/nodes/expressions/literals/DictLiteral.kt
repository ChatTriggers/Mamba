package com.chattriggers.mamba.ast.nodes.expressions.literals

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.collections.VDictType
import com.chattriggers.mamba.core.values.exceptions.notImplemented

class DictLiteral(
    lineNumber: Int,
    private val dict: Map<ExpressionNode, ExpressionNode>
) : ExpressionNode(lineNumber, (dict.keys + dict.values).toList()) {
    override fun execute(ctx: ThreadContext): VObject {
        val map = dict.mapKeys { (key) ->
            if (key !is IdentifierNode && key !is StringLiteral)
                notImplemented()

            (key.execute(ctx) as VString).string
        }.mapValues { (_, value) ->
            value.execute(ctx)
        }.toMutableMap()

        return ctx.runtime.construct(VDictType, listOf(map))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        dict.forEach { (key, value) ->
            key.print(indent + 1)
            value.print(indent + 2)
        }
    }
}