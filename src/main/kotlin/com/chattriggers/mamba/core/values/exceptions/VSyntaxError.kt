package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VSyntaxError(args: VTuple) : VException(args, LazyValue("VSyntaxErrorType") { VSyntaxErrorType }) {
    override val className = "SyntaxError"
}

object VSyntaxErrorType : VType(LazyValue("VExceptionType") { VExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VSyntaxErrorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VSyntaxErrorType) {
                TODO()
            }

            VSyntaxError(arguments().toValue())
        }
        addMethod("__init__") {
            VNone
        }
    }
}