package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ir.nodes.ScriptNode
import java.util.*

class Interpreter private constructor(private val script: ScriptNode) {
    internal val scopeStack = Stack<VObject>()
    internal val runtime = Runtime(this)

    init {
        scopeStack.push(GlobalScope)
    }

    private fun execute(): Any {
        return script.execute(this)
    }

    internal fun pushScope(scope: VObject = VObject()) {
        scopeStack.push(scope)
    }

    internal fun popScope(): VObject {
        if (scopeStack.empty()) {
            TODO("Error")
        }

        return scopeStack.pop()
    }

    internal fun getScope() = scopeStack.peek()

    companion object {
        fun execute(node: ScriptNode): Any {
            return Interpreter(node).execute()
        }
    }
}