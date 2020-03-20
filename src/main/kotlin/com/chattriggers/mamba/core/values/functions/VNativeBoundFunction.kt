package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VObject

// User-defined function
class VNativeBoundFunction(
    private val name: String,
    private val func: NativeClassMethod,
    private val thisObj: VObject
) : VObject(FunctionDescriptor), ICallable {
    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return func.invoke(ClassMethodBuilder(interp, listOf(thisObj) + args))
    }

    override fun toString() = "<method '$name' of '<TODO: className field>' objects>"
}