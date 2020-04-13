package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.collections.toValue

class VNameError : VException(LazyValue("VNameErrorType") { VNameErrorType }) {
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
            VNameError()
        }
        addMethod("__init__") {
            assertSelfAs<VNameError>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}