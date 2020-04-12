package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

class VTypeError(args: VTuple) : VException(args, LazyValue("VTypeErrorType") { VTypeErrorType }) {
    override val className = "TypeError"

    companion object {
        fun construct(message: String): VTypeError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VTypeErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    listOf(message.toValue())
                ))
            )) as VTypeError
        }
    }
}

object VTypeErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VTypeErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VTypeErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VTypeError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}