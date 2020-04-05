package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.VObject

class ClassMethodBuilder(val interp: Interpreter, private val args: List<VObject>) {
    val runtime: Runtime
        get() = interp.runtime

    fun argument(index: Int): VObject {
        if (index >= args.size)
            TODO()
        return args[index]
    }

    fun arguments(): List<VObject> {
        return args
    }

    fun argumentsCount() = args.size

    // Used by number classes
    fun widenFirstArgs(): Pair<VObject, VObject> {
        return runtime.widen(argument(0), argument(1))
    }

    inline fun <reified T : VObject> argAs(index: Int): T? {
        if (index >= argumentsCount()) return null
        return assertArgAs<T>(index)
    }

    inline fun <reified T : VObject> assertArgAs(index: Int): T {
        val arg = argument(index)
        if (arg !is T)
            TODO()
        return arg
    }

    inline fun <reified T : VObject> assertSelfAs(): T {
        return assertArgAs(0)
    }
}

typealias NativeClassMethod = ClassMethodBuilder.() -> VObject