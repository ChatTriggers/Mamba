package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.Value

class VDict(val map: MutableMap<String, Value>) : VObject() {
    override val className: String
        get() = "dict"

    override fun toString(): String {
        // TODO: Serialization
        return map.toString()
    }
}

fun MutableMap<String, Value>.toValue() =
    VDict(this)
