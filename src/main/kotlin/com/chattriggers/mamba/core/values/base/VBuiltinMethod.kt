package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.*
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.singletons.VNone

class VBuiltinMethod(
    val method: MethodWrapper
) : VObject(LazyValue("VNativeMethodType") { VBuiltinMethodType }) {
    override val className = "builtin_function_or_method" // from CPython

    override fun toString() = "<builtin-in method ${method.name} of TODO[ at TODO]>"
}

object VBuiltinMethodType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            val self = assertSelfAs<VBuiltinMethod>()
            val method = self.method
            method.call(ctx, arguments().let { it.subList(1, it.size) })
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VBuiltinMethodType>(0)
            VBuiltinMethod(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}