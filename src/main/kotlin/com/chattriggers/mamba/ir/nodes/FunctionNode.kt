package com.chattriggers.mamba.ir.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.functions.VFunction
import com.chattriggers.mamba.ir.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.ir.nodes.statements.StatementNode

data class FunctionNode(
    private val identifier: IdentifierNode,
    internal val statements: List<StatementNode>
) : StatementNode(listOf(identifier) + statements), ICallable {
    private var returnedValue: Value? = null

    override val name: String
        get() = identifier.identifier

    override fun call(interp: Interpreter, args: List<Value>): Value {
        returnedValue = null

        val scope = VObject()
        // TODO: Populate arguments
        // Accept a list of identifiers in the constructor,
        // and associate each one of those with the corresponding
        // arguments in the scope
        interp.pushScope(scope)

        var i = 0

        while (returnedValue == null && i < statements.size) {
            statements[i++].execute(interp)
        }

        interp.popScope()

        return returnedValue ?: VNone
    }

    internal fun returnValue(value: Value) {
        if (returnedValue != null)
            TODO("Error")
        returnedValue = value
    }

    override fun execute(interp: Interpreter): Value {
        val scope = interp.getScope()
        scope[identifier.identifier] = VFunction(identifier.identifier, this)
        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"${identifier.identifier}\"")
        statements.forEach { it.print(indent + 1) }
    }

    override fun toString() = "<function $identifier>"
}
