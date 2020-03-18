package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.VNativeFuncType
import com.chattriggers.mamba.core.values.functions.VNativeMethod

/**
 * Represents "normal" Python objects. Classes that
 * inherit from this class are able to be created by
 * user scripts.
 */
open class VObject : Value() {
    open val className: String = "object"

    init {
        addNativeMethod("__dir__") { _, args ->
            val self = assertSelf<VObject>(args)
            self.slotKeys().map(::VString).toValue()
        }
    }

    fun addProperty(name: String, value: Value) {
        slots[name] = value
    }

    fun addNativeMethod(name: String, func: VNativeFuncType) {
        slots[name] = VNativeMethod(name, this, func)
    }

    override fun toString() = "<object object>"
}
