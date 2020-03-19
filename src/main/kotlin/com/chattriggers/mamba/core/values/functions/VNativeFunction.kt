package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.ClassDescriptor
import com.chattriggers.mamba.core.values.ObjectDescriptor
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ir.nodes.FunctionNode

// User-defined function
class VNativeFunction(
    private val name: String,
    private val func: (interp: Interpreter, args: List<VObject>) -> VObject
) : VObject(),
    ICallable {
    override val descriptor: ClassDescriptor
        get() = FunctionDescriptor // TODO: Does this need it's own descriptor?

    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return func.invoke(interp, args)
    }
}
