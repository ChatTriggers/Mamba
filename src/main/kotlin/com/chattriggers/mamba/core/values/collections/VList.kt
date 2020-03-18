package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.Value

class VList<T : Value>(val list: MutableList<T>) : VObject() {
    override val className: String
        get() = "list"

    override fun toString(): String {
        return StringBuilder().apply {
            append('[')

            list.forEach {
                append(it.toString())
            }

            append(']')
        }.toString()
    }
}

fun <T : Value> MutableList<T>.toValue() = VList(this)

// Without the dummy parameter, it doesn't compile
fun <T : Value> List<T>.toValue(b: Boolean = true) = VList(this.toMutableList())
