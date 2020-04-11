package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.ast.nodes.expressions.Argument
import com.chattriggers.mamba.ast.nodes.expressions.ExpressionNode
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VFunctionType
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType

data class ParameterNode(
    val identifier: IdentifierNode,
    val defaultValue: ExpressionNode? = null
)

class FunctionNode(
    lineNumber: Int,
    private val identifier: IdentifierNode,
    private val parameters: List<ParameterNode>,
    internal val statements: List<StatementNode>
) : StatementNode(lineNumber, listOf(identifier) + statements) {
    private lateinit var capturedScopes: List<VObject>

    fun call(ctx: ThreadContext, args: List<Argument>): VObject {
        if (args.any { it.spread } || args.any { it.kwSpread })
            TODO("Support spread args")

        val scope = ctx.runtime.construct(VObjectType)

        for ((index, param) in parameters.withIndex()) {
            var value: VObject

            // Make sure positional arguments are filled
            if (index >= args.size && param.defaultValue == null) {
                TODO("Error: Missing required positional argument")
            }

            // Check for arguments with same name
            if (index >= args.size) {
                // Look for named argument with the same name as
                // this parameter
                val argSameName = args.firstOrNull {
                    it.name == param.identifier.identifier
                }

                if (argSameName == null) {
                    // Use default value
                    value = param.defaultValue!!.execute(ctx)
                    if (value is VExceptionWrapper)
                        return value
                } else {
                    value = argSameName.value.unwrap()
                }
            } else {
                // We have an argument at this index, but we
                // first need to check for named argument with the
                // same name as this parameter
                // TODO: Is this necessary?
                val argSameName = args.firstOrNull {
                    it.name == param.identifier.identifier
                }

                value = when {
                    // Found named argument
                    argSameName != null -> argSameName.value.unwrap()

                    // Use positional argument, only if unnamed
                    args[index].name == null -> args[index].value.unwrap()

                    // Use default value
                    else -> {
                        val defaultValue = param.defaultValue!!.execute(ctx)
                        if (defaultValue is VExceptionWrapper)
                            return defaultValue
                        defaultValue
                    }
                }
            }

            scope.putSlot(param.identifier.identifier, value)
        }

        // Save current scopes and load captured scopes
        val currScopes = ctx.interp.scopes.captureScopes()

        ctx.interp.scopes.loadScopes(capturedScopes)
        ctx.interp.scopes.pushScope(scope)

        try {
            return when (val returned = executeStatements(ctx, statements)) {
                is VReturnWrapper -> returned.wrapped
                is VExceptionWrapper -> return returned
                is VFlowWrapper -> TODO() // Should have been handled by an enclosing node
                else -> VNone
            }
        } finally {
            ctx.interp.scopes.loadScopes(currScopes)
        }
    }

    override fun execute(ctx: ThreadContext): VObject {
        capturedScopes = ctx.interp.scopes.captureScopes()

        val ident = identifier.identifier
        ctx.interp.scopes.currScope.putSlot(ident, MethodWrapper(ident, this))
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
