package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

class VAttributeError(args: VTuple) : VException(args, LazyValue("VAttributeErrorType") { VAttributeErrorType }) {
    override val className = "AttributeError"

    constructor(identifier: String, type: String) : this(ThreadContext.currentContext.runtime.construct(
        VTupleType,
        listOf("'$type' object has no attribute '$identifier'")
    ) as VTuple)
}

object VAttributeErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VAttributeErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VAttributeErrorType) {
                notImplemented()
            }

            VAttributeError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}