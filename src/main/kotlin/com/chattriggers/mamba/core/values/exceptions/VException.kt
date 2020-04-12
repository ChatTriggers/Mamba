package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
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
        addMethod("__new__", isStatic = true) {
            assertArgAs<VExceptionType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VException(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}
