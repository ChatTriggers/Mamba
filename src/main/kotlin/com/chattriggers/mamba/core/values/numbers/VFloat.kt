package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.VObject

open class VFloat(internal val num: Double) : VObject() {
    override val className: String
        get() = "int"

    override fun toString() = num.toString()
}

fun Double.toValue() = VFloat(this)
fun Float.toValue() = VFloat(this.toDouble())
