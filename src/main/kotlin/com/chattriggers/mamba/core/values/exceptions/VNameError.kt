package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class VNameError(args: VTuple) : VException(args, LazyValue("VNameErrorType") { VNameErrorType }) {
    override val className = "NameError"

    companion object {
        fun construct(name: String): VNameError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VNameErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    listOf("name '$name' is not defined".toValue())
                ))
            )) as VNameError
        }
    }
}

object VNameErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VNameErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VNameErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VNameError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}