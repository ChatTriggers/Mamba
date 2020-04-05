package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.ScriptNode
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import java.util.*

class Interpreter private constructor(private val script: ScriptNode, val fileName: String, val lines: List<String>) {
    internal val runtime = Runtime(this)

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

    internal inline fun <reified T : VBaseException> throwException(lineNumber: Int, vararg args: VObject): Nothing {
        exceptionStack.push(CallFrame(fileName, sourceStack.pop(), lineNumber))

        throw MambaException(
            T::class.java.getDeclaredConstructor(VTuple::class.java).newInstance(VTuple(*args))
        )
    }

    internal fun throwException(exception: VBaseException, lineNumber: Int): Nothing {
        exceptionStack.push(CallFrame(fileName, sourceStack.pop(), lineNumber))
        throw MambaException(exception)
    }

    companion object {
        fun execute(node: ScriptNode, fileName: String, lines: List<String>): Any {
            return Interpreter(node, fileName, lines).execute()
        }
    }
}