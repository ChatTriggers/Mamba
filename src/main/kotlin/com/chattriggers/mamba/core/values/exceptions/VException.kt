package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone

open class VException(
    type: LazyValue<VType> = LazyValue("VBaseExceptionType") { VBaseExceptionType }
) : VBaseException(type) {
    override val className = "Exception"
}

object VExceptionType : VType(LazyValue("VBaseExceptionType") { VBaseExceptionType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VExceptionType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VExceptionType>(0)
            VException()
        }
        addMethod("__init__") {
            assertSelfAs<VException>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}
