package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
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
            assertArgAs<VLookupErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VLookupError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}