package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone

class VNotImplementedError: VException(LazyValue("VNotImplementedErrorType") { VNotImplementedErrorType }) {
    override val className = "NotImplementedError"
}

object VNotImplementedErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VNotImplementedErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VNotImplementedErrorType>(0)
            VNotImplementedError()
        }
        addMethod("__init__") {
            assertSelfAs<VNotImplementedError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}