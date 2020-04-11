package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

class VSyntaxError(args: VTuple) : VException(args, LazyValue("VSyntaxErrorType") { VSyntaxErrorType }) {
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
            runtime.construct(VNameErrorType, arguments().let { it.subList(1, it.size) })
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VSyntaxErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VSyntaxError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}