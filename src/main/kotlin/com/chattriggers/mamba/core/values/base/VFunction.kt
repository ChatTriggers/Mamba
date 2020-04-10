package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VFunction(
    val function: MethodWrapper
) : VObject(LazyValue("VMethodType") { VFunctionType }) {
    override val className = "function"

    override fun toString() = "<function TODO[at TODO]>"
}

object VFunctionType: VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            val self = assertSelfAs<VFunction>()
            val method = self.function
            method.call(ctx, arguments())
        }
        addMethod("__new__") {
            assertArgAs<VFunctionType>(0)
            VFunction(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}