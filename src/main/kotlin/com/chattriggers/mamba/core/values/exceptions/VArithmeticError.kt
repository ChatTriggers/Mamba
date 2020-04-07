package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VDictType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VArithmeticError(args: VTuple) : VException(args, LazyValue("VArithmeticErrorType") { VArithmeticErrorType }) {
    override val className = "ArithmeticError"

    constructor() : this(VTuple())
}

object VArithmeticErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VArithmeticErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VArithmeticErrorType) {
                notImplemented()
            }

            VArithmeticError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}