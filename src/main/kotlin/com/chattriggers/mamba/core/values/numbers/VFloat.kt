package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.numbers.toValue

class VFloat(val double: Double) : VObject(FloatDescriptor) {
    override fun toString() = double.toString()
}

object FloatDescriptor : ClassDescriptor(ComplexDescriptor)

fun Double.toValue() = VFloat(this)

fun Float.toValue() = VFloat(this.toDouble())
