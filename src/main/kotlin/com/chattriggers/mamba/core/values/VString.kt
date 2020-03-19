package com.chattriggers.mamba.core.values

class VString(internal val string: String) : VObject() {
    override val className: String
        get() = "str"

    override fun toString() = string
}

fun String.toValue() = VString(this)
