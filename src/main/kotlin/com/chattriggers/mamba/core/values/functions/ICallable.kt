package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

/**
 * Represents any object that can be called.
 * This includes both native functions and
 * user-defined functions
 */
interface ICallable {
    fun call(interp: Interpreter, args: List<VObject>): VObject
}