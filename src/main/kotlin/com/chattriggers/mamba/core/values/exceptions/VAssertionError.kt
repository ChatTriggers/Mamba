package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class VAssertionError : VException(LazyValue("VAssertionErrorType") { VAssertionErrorType }) {
    override val className = "AssertionError"

    companion object {
        fun construct(arg: VObject? = null): VAssertionError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VAssertionErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    if (arg != null) {
                        listOf(arg)
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
            VAssertionError()
        }
        addMethod("__init__") {
            assertSelfAs<VAssertionError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}