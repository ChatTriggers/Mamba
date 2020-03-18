package com.chattriggers.mamba.core.values

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
        }
    }

    override fun toString(): String {
        // TODO: Serialization
        return "OBJECT"
    }
}
