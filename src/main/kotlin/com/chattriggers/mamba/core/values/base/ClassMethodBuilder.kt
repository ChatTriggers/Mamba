package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.unwrap

class ClassMethodBuilder(val interp: Interpreter, private val _args: List<Value>) {
    val runtime = interp.runtime

    val argSize = _args.size

    fun argument(index: Int) = _args[index] as VObject

    fun argumentRaw(index: Int) = _args[index]

    fun arguments() = _args.map(Value::unwrap)

    // Used by number classes
    fun widenFirstArgs(): Pair<VObject, VObject> {
        return runtime.widen(argument(0), argument(1))
    }

    inline fun <reified T : VObject> argAs(index: Int): T? {
        if (index >= argSize) return null
        return assertArgAs<T>(index)
    }

    inline fun <reified T : VObject> assertArgAs(index: Int): T {
        val arg = argument(index)
        if (arg !is T)
            notImplemented()
        return arg
    }

    inline fun <reified T : VObject> assertSelfAs(): T {
        return assertArgAs(0)
    }
}

typealias NativeClassMethod = ClassMethodBuilder.() -> VObject