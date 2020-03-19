package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.functions.ICallable

/**
 * Represents a Mamba runtime object. Every object
 * inherits from this class
 *
 * @param slots Internal slotmap. This is not meant to be passed in,
 *      as it is only a constructor property to allow MutableMap
 *      implementation delegation
 */
abstract class Value() : MutableMap<String, Value> by mutableMapOf() {
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

    companion object {
        inline fun <reified T : Value> assertSelf(args: List<Value>): T {
            if (args.isEmpty() || args[0] !is T)
                TODO("Error")
            return args[0] as T
        }

        inline fun <reified T : Value> assertArg(args: List<Value>, index: Int): T {
            if (index >= args.size || args[index] !is T)
                TODO("Error")
            return args[index] as T
        }
    }
}
