package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VNotImplementedError(
    args: VTuple
) : VException(args, LazyValue("VNotImplementedErrorType") { VNotImplementedErrorType }) {
    override val className = "NotImplementedError"
}

object VNotImplementedErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VNotImplementedErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VNotImplementedErrorType) {
                TODO()
            }

            VNotImplementedError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}