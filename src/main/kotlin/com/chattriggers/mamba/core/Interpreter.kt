package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.ScriptNode
import com.chattriggers.mamba.core.values.base.VObject

class Scopes {
    var scopeStack = mutableListOf<VObject>()

    val currScope: VObject
        get() = scopeStack.last()

    init {
        scopeStack.add(GlobalScope)
    }

    fun captureScopes() = scopeStack.toList()

    fun loadScopes(scopes: List<VObject>) {
        scopeStack = scopes.toMutableList()
    }

    fun pushScope(scope: VObject) {
        scopeStack.add(scope)
    }

    fun popScope() = scopeStack.removeAt(scopeStack.lastIndex)
}

class Interpreter(val fileName: String, val lines: List<String>) {
    val scopes = Scopes()

    fun execute(script: ScriptNode): Any {
        return script.execute(ThreadContext.currentContext)
    }
}