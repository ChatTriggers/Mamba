package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.unwrap

class ClassMethodBuilder(val ctx: ThreadContext, private val _args: List<Value>) {
    val runtime = ctx.runtime

    val argSize = _args.size

    fun argument(index: Int) = _args[index] as VObject

    fun argumentRaw(index: Int) = _args[index]

    fun arguments() = _args.map(Value::unwrap)

    inline fun <reified T : VObject> argAs(index: Int): T? {
        if (index >= argSize) return null
        return assertArgAs<T>(index)
    }

    inline fun <reified T : Value> assertArgAs(index: Int): T {
        val arg = argument(index)
        if (arg !is T)
            notImplemented()
        return arg
    }

    inline fun <reified T : Value> assertSelfAs(): T {
        return assertArgAs(0)
    }

    fun construct(type: VType, vararg args: Any): VObject {
        return runtime.construct(type, args.map {
            if (it !is VObject) Wrapper(it) else it
        })
    }
}

typealias NativeClassMethod = ClassMethodBuilder.() -> VObject