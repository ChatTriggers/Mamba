package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.VNativeFuncType
import com.chattriggers.mamba.core.values.functions.VNativeMethod

abstract class VType : VObject() {
    abstract val className: String

    fun addNativeMethod(name: String, func: VNativeFuncType) {
        slots[name] = VNativeMethod(name, this, func)
    }

    override fun toString(): String {
        return "<class '$className'>"
    }

    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "type"

            init {
                slots["__dict__"] = slots.toValue()
                slots["__base__"] = VObject.TYPE
            }
        }
    }
}
