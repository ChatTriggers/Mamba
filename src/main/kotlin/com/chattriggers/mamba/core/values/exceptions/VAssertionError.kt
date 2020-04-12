package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

class VAssertionError(args: VTuple) : VException(args, LazyValue("VAssertionErrorType") { VAssertionErrorType }) {
    override val className = "AssertionError"

    companion object {
        fun construct(message: String? = null): VAssertionError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VAssertionErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    if (message != null) {
                        listOf(message.toValue())
                    } else {
                        emptyList()
                    }
                ))
            )) as VAssertionError
        }
    }
}

object VAssertionErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VAssertionErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VAssertionErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VAssertionError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}