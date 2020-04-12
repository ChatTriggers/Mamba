package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.singletons.VNone

class VArithmeticError(args: VTuple) : VException(args, LazyValue("VArithmeticErrorType") { VArithmeticErrorType }) {
    override val className = "ArithmeticError"
}

object VArithmeticErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VArithmeticErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VArithmeticErrorType>(0)
            VArithmeticError(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}