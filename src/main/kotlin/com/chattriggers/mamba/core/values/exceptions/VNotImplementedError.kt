package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue

class VNotImplementedError(args: VTuple) : VException(args) {
    override val className = "NotImplementedError"

    constructor() : this(VTuple())
}

object VNotImplementedErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethodDescriptor("__call__") {
            VNotImplementedError(arguments().toValue())
        }
    }
}