package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone

class VStopIteration(args: VTuple) : VException(args, LazyValue("VStopIterationType") { VStopIterationType }) {
    override val className = "StopIteration"

    constructor() : this(ThreadContext.currentContext.runtime.construct(VTupleType, emptyList()) as VTuple)

    companion object {
        fun construct(): VStopIteration {
            val rt = ThreadContext.currentContext.runtime

            return rt.construct(VStopIterationType, listOf(
                rt.construct(VTupleType, listOf(emptyList<VObject>()))
            )) as VStopIteration
        }
    }
}

object VStopIterationType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VStopIterationType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VStopIterationType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VStopIteration(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}