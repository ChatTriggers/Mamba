package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VValueError(args: VTuple) : VException(args, LazyValue("VValueErrorType") { VValueErrorType }) {
    override val className = "ValueError"

    constructor() : this(VTuple())
}

object VValueErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VValueErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VValueErrorType) {
                notImplemented()
            }

            VValueError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}