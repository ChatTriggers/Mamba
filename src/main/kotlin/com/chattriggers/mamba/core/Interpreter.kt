package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.ScriptNode
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import java.util.*

class Interpreter private constructor(private val script: ScriptNode) {
    private val scopeStack = Stack<VObject>()
    internal val scopeStacks: List<VObject>
        get() = scopeStack.reversed()
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
            notImplemented("Error")
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