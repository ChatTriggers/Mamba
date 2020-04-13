package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone

class VLookupError : VException(LazyValue("VLookupErrorType") { VLookupErrorType }) {
    override val className = "LookupError"
}

object VLookupErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VLookupErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VLookupErrorType>(0)
            VLookupError()
        }
        addMethod("__init__") {
            assertSelfAs<VLookupError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}