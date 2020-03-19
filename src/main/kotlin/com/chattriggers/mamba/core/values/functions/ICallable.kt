package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

interface ICallable {
    fun call(interp: Interpreter, args: List<VObject>): VObject
}