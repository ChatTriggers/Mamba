package com.chattriggers.mamba.core.values

class VString(val string: String) : Value() {
    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "str"
        }
    }

    override fun toString() = string
}

fun String.toValue() = VString(this)
