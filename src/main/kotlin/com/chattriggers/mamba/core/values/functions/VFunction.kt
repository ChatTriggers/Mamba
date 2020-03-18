package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.ir.nodes.FunctionNode

open class VFunction(name: String, val functionNode: FunctionNode) : VCallable(name) {
    override fun call(interp: Interpreter, args: List<Value>): Value {
        return functionNode.call(interp, args)
    }

    override fun toString() = "<function $name>"
}