package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class VSyntaxError : VException(LazyValue("VSyntaxErrorType") { VSyntaxErrorType }) {
    override val className = "SyntaxError"

    companion object {
        fun construct(message: String): VTypeError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VSyntaxErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    listOf(message.toValue())
                ))
            )) as VTypeError
        }
    }
}

object VSyntaxErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VNameErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VSyntaxErrorType>(0)
            VSyntaxError()
        }
        addMethod("__init__") {
            assertSelfAs<VSyntaxError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}