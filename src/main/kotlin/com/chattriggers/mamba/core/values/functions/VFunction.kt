package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.ClassDescriptor
import com.chattriggers.mamba.core.values.ObjectDescriptor
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ir.nodes.FunctionNode

// User-defined function
class VFunction(
    private val name: String,
    private val func: FunctionNode
) : VObject(),
    ICallable {
    override val descriptor: ClassDescriptor
        get() = FunctionDescriptor

    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return func.call(interp, args)
    }
}

object FunctionDescriptor : ClassDescriptor(ObjectDescriptor) {

}
