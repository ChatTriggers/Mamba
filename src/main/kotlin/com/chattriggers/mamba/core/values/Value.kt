package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import java.util.concurrent.Callable

/**
 * Represents a Mamba runtime object. Every object
 * inherits from this class
 *
 * @param slots Internal slotmap. This is not meant to be passed in,
 *      as it is only a constructor property to allow MutableMap
 *      implementation delegation
 */
abstract class Value(
    protected val slots: MutableMap<String, Value> = mutableMapOf()
) : MutableMap<String, Value> by slots {
    abstract override fun toString(): String

    fun callFunction(interp: Interpreter, name: String, args: List<Value> = emptyList()): Value {
        val result = callFunctionOrNone(interp, name, args)
        if (result == VNone) {
            TODO("Error")
        }
        return result
    }

    fun callFunctionOrNone(interp: Interpreter, name: String, args: List<Value> = emptyList()): Value {
        val result = this[name] ?: return VNone

        if (result !is ICallable) {
            TODO("Error")
        }

        // Add "self" arguments
        return result.call(interp, listOf(this) + args)
    }

    protected fun inherit(type: VType) {
        type.slots.forEach { (key, value) ->
            this.slots[key] = value
        }
    }

    companion object {
        inline fun <reified T : Value> assertSelf(args: List<Value>): T {
            if (args.isEmpty() || args[0] !is T)
                TODO("Error")
            return args[0] as T
        }
    }
}
