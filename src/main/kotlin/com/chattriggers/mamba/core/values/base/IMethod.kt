package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.Value

/**
 * Represents any object that can be called.
 * This includes both native functions and
 * user-defined functions
 */
interface IMethod {
    var self: VObject?

    fun call(interp: Interpreter, args: List<Value>): VObject

    fun bind(newSelf: VObject): VObject
}