package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue

class VArithmeticError(args: VTuple) : VException(args) {
    override val className = "ArithmeticError"

    constructor() : this(VTuple())
}

object VArithmeticErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethodDescriptor("__call__") {
            VArithmeticError(arguments().toValue())
        }
    }
}