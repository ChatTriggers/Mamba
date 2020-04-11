package com.chattriggers.mamba.core

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