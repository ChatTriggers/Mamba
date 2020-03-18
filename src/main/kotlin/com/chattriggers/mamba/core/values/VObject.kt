package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.collections.toValue

/**
 * Represents "normal" Python objects. Classes that
 * inherit from this class are able to be created by
 * user scripts.
 */
open class VObject : Value() {
    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "object"

            init {
                addNativeMethod("__dir__") { interp, args ->
                    val self = assertSelf<VObject>(args)
                    self.slotKeys().map(::VString).toValue()
                }
            }
        }
    }

    override fun toString(): String {
        // TODO: Serialization
        return "OBJECT"
    }
}
