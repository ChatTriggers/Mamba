package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VLookupError(args: VTuple) : VException(args, LazyValue("VLookupErrorType") { VLookupErrorType }) {
    override val className = "LookupError"
}

object VLookupErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VLookupErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VLookupErrorType) {
                TODO()
            }

            VLookupError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}