package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VStopIteration(args: VTuple) : VException(args, LazyValue("VStopIterationType") { VStopIterationType }) {
    override val className = "StopIteration"

    constructor() : this(VTuple())
}

object VStopIterationType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VStopIterationType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VStopIterationType) {
                notImplemented()
            }

            VStopIteration(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}