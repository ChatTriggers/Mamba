package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import java.lang.IllegalStateException

class VNativeFunction(val funcName: String, val func: (Interpreter, List<Value>) -> Value) : VNative(), ICallable {
    override fun toString(): String {
        return "<built-in function $funcName>"
    }

    override fun call(interp: Interpreter, args: List<Value>): Value {
        return func.invoke(interp, args)
    }

    override fun returnValue(value: Value) {
        // TODO: This should never be called?
        // This method should only be used from a ReturnNode,
        // which obviously will never be present here
        throw IllegalStateException()
    }
}
