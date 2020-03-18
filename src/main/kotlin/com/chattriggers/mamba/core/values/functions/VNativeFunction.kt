package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*

typealias VNativeFuncType = (Interpreter, List<Value>) -> Value

open class VNativeFunction(name: String, val function: VNativeFuncType) : VCallable(name) {
    override fun toString() = "<built-in function $name>"

    override fun call(interp: Interpreter, args: List<Value>): Value {
        return function.invoke(interp, args)
    }
}