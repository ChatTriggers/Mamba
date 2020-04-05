package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*

class VDict(val dict: MutableMap<String, Value>) : VObject(LazyValue("VDictType") { VDictType }) {
    override val className = "dict"

    override fun toString() = StringBuilder().apply {
        append("{")

        dict.entries.forEachIndexed { index, (key, value) ->
            append(key)
            append(": ")
            append(value)

            if (index < dict.size - 1)
                append(", ")
        }

        append("}")
    }.toString()
}

object VDictType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            VDictIterator(assertSelf())
        }
    }
}

fun Map<String, VObject>.toValue() = VDict(this.toMutableMap())
