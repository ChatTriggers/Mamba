package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.collections.toValue
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
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VStopIterationType) {
                TODO()
            }

            VStopIteration(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}