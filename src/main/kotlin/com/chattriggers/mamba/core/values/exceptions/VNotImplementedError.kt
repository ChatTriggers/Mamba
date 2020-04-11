package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
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
            assertArgAs<VNotImplementedErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VNotImplementedError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}