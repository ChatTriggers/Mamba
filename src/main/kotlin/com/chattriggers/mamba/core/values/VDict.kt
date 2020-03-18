package com.chattriggers.mamba.core.values

class VDict(val map: MutableMap<String, Value>) : VObject() {
    override fun toString(): String {
        // TODO: Serialization
        return map.toString()
    }
}

fun MutableMap<String, Value>.toValue() = VDict(this)
