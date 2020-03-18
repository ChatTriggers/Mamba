package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter

interface ICallable {
    fun call(interp: Interpreter, args: List<Value>): Value

    fun returnValue(value: Value)
}