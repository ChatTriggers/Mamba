package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class VValueError : VException(LazyValue("VValueErrorType") { VValueErrorType }) {
    override val className = "ValueError"

    companion object {
        fun construct(message: String): VValueError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VValueErrorType, listOf(
                rt.construct(VTupleType, listOf(listOf(message.toValue())))
            )) as VValueError
        }
    }
}

object VValueErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VValueErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VValueErrorType>(0)
            VValueError()
        }
        addMethod("__init__") {
            assertSelfAs<VValueError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}