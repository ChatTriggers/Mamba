package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue

class VSyntaxError(args: VTuple) : VException(args) {
    override val className = "SyntaxError"

    constructor() : this(VTuple())
}

object VSyntaxErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethodDescriptor("__call__") {
            VSyntaxError(arguments().toValue())
        }
    }
}