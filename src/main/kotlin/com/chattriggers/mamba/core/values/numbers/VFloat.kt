package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.*

class VFloat(val double: Double) : VObject(LazyValue("VFloatType") { VFloatType }) {
    override fun toString() = double.toString()
}

object VFloatType : VType(LazyValue("VComplexType") { VComplexType })

fun Double.toValue() = VFloat(this)

fun Float.toValue() = VFloat(this.toDouble())
