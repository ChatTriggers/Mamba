package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

class VTypeError(args: VTuple) : VException(args, LazyValue("VTypeErrorType") { VTypeErrorType }) {
    override val className = "TypeError"

    constructor() : this(VTuple())

    constructor(msg: String) : this(VTuple(msg.toValue()))
}

object VTypeErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VTypeErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VTypeErrorType) {
                notImplemented()
            }

            VTypeError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}