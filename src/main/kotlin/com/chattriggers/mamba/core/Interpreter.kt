package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.ast.nodes.ScriptNode
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import java.util.*

class Interpreter(val fileName: String, val lines: List<String>) {
    internal val scopeStack: List<VObject>
        get() = scopeStackBacker.reversed()

    private val scopeStackBacker = Stack<VObject>()

    // Stacktrace data
    internal val callStack = Stack<CallFrame>()
    internal val exceptionStack = Stack<CallFrame>()
    internal val sourceStack = Stack<String>()

    init {
        scopeStackBacker.push(GlobalScope)
        sourceStack.push("<module>")
    }

    fun execute(script: ScriptNode): Any {
        return script.execute(ThreadContext.currentContext)
    }

    internal fun pushScope(scope: VObject = ThreadContext.currentContext.runtime.construct(VObjectType)) {
        scopeStackBacker.push(scope)
    }

    internal fun popScope(): VObject {
        if (scopeStackBacker.empty()) {
            TODO("Error")
        }

        return scopeStackBacker.pop()
    }

    internal fun getScope() = scopeStackBacker.peek()

    internal fun throwException(exception: VBaseException, lineNumber: Int): Nothing {
        exceptionStack.push(CallFrame(fileName, sourceStack.pop(), lineNumber))
        throw MambaException(exception)
    }
}