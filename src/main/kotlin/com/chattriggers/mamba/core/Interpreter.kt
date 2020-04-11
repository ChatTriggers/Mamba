package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.ScriptNode

class Interpreter(val fileName: String, val lines: List<String>) {
    val scopes = Scopes()

    fun execute(script: ScriptNode): Any {
        return script.execute(ThreadContext.currentContext)
    }
}