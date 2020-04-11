package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.ScriptNode
import java.util.*

class Interpreter(val fileName: String, val lines: List<String>) {
    val scopes = Scopes()

    val callStack = Stack<CallFrame>()

    init {
        callStack.push(CallFrame("<module>", fileName, -1))
    }

    fun execute(script: ScriptNode): Any {
        return script.execute(ThreadContext.currentContext)
    }
}

data class CallFrame(
    val name: String,
    val source: String,
    var lineNumber: Int
)
