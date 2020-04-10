package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

open class VException(
    args: VTuple,
    type: LazyValue<VType> = LazyValue("VBaseExceptionType") { VBaseExceptionType }
) : VBaseException(args, type) {
    override val className = "Exception"
}

object VExceptionType : VType(LazyValue("VBaseExceptionType") { VBaseExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VExceptionType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VExceptionType) {
                TODO()
            }

            VException(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}
