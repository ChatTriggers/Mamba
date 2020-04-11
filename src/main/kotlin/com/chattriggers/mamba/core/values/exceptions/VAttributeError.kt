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

class VAttributeError(args: VTuple) : VException(args, LazyValue("VAttributeErrorType") { VAttributeErrorType }) {
    override val className = "AttributeError"

    companion object {
        fun construct(identifier: String, type: String): VAttributeError {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VAttributeErrorType, listOf(
                rt.construct(VTupleType, listOf(
                    listOf("'$type' object has no attribute '$identifier".toValue())
                ))
            )) as VAttributeError
        }
    }
}

object VAttributeErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VAttributeErrorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VAttributeErrorType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VAttributeError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}