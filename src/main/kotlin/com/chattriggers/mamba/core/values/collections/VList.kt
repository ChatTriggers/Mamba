package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.Value

class VList<T : Value>(val list: MutableList<T>) : Value() {
    override fun toString(): String {
        return StringBuilder().apply {
            append('[')

            list.forEach {
                append(it.toString())
            }

            append(']')
        }.toString()
    }

    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "list"
        }
    }
}

fun <T : Value> MutableList<T>.toValue() = VList(this)

// Without the dummy parameter, it doesn't compile
fun <T : Value> List<T>.toValue(b: Boolean = true) = VList(this.toMutableList())
