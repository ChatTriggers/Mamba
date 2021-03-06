package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VBuiltinMethod : VObject(LazyValue("VNativeMethodType") { VBuiltinMethodType }) {
    override val className = "builtin_function_or_method" // from CPython

    lateinit var method: MethodWrapper

    override fun toString() = "<builtin-in method ${method.name} of TODO[ at TODO]>"
}

object VBuiltinMethodType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            val self = assertSelfAs<VBuiltinMethod>()
            val method = self.method
            method.call(ctx, argumentsRaw().let { it.subList(1, it.size) })
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VBuiltinMethodType>(0)
            VBuiltinMethod()
        }
        addMethod("__init__") {
            assertSelfAs<VBuiltinMethod>().method = assertArgAs(1)
            VNone
        }
    }
}