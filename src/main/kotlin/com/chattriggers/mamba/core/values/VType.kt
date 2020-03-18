package com.chattriggers.mamba.core.values

abstract class VType : VObject() {
    abstract val className: String

    override fun toString(): String {
        return "<class<'$className'>"
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
