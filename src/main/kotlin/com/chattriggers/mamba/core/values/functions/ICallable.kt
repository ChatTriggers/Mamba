package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.Value

/**
 * Interface for any callable object that can exist in
 * the Mamba runtime
 */
interface ICallable {
    val name: String

    fun call(interp: Interpreter, args: List<Value>): Value
}