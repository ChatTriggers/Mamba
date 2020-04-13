package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VFunction : VObject(LazyValue("VMethodType") { VFunctionType }) {
    override val className = "function"

    lateinit var function: MethodWrapper

    override fun toString() = "<function TODO[at TODO]>"
}

object VFunctionType: VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            val self = assertSelfAs<VFunction>()
            val method = self.function
            method.call(ctx, argumentsRaw().let { it.subList(1, it.size) })
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VFunctionType>(0)
            VFunction()
        }
        addMethod("__init__") {
            assertSelfAs<VFunction>().function = assertArgAs(1)
            VNone
        }
    }
}