package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VNameError(args: VTuple) : VException(args, LazyValue("VNameErrorType") { VNameErrorType }) {
    override val className = "NameError"

    constructor() : this(VTuple())
}

object VNameErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VNameErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VNameErrorType) {
                notImplemented()
            }

            VNameError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}