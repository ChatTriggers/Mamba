package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.Value

class VDict<T : Value>(internal val map: MutableMap<String, T>) : VObject() {
    override val className: String
        get() = "dict"

    override fun toString(): String {
        // TODO: Serialization
        return map.toString()
    }
}

fun <T : Value> MutableMap<String, T>.toValue() =
    VDict(this)
