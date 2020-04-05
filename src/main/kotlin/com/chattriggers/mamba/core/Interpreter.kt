package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.ScriptNode
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import java.util.*

class Interpreter private constructor(private val script: ScriptNode, val fileName: String, val lines: List<String>) {
    internal val runtime = Runtime(this)

    internal val scopeStack: List<VObject>
        get() = scopeStackBacker.reversed()

    internal val callStack: Stack<CallFrame>
        get() = callStackBacker

    private val scopeStackBacker = Stack<VObject>()
    private val callStackBacker = Stack<CallFrame>()

    init {
        scopeStackBacker.push(GlobalScope)
    }

    private fun execute(): Any {
        return script.execute(this)
    }

    internal fun pushScope(scope: VObject = VObject()) {
        scopeStackBacker.push(scope)
    }

    internal fun popScope(): VObject {
        if (scopeStackBacker.empty()) {
            notImplemented("Error")
        }

        return scopeStackBacker.pop()
    }

    internal fun getScope() = scopeStackBacker.peek()

    internal fun pushCallStack(file: String, source: String, lineNumber: Int) {
        callStackBacker.push(CallFrame(file, source, lineNumber))
    }

    internal fun popCallStack() {
        callStackBacker.pop()
    }

    companion object {
        fun execute(node: ScriptNode, fileName: String, lines: List<String>): Any {
            return Interpreter(node, fileName, lines).execute()
        }
    }
}