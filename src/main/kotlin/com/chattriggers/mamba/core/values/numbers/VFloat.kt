package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType

class VFloat(val double: Double) : VObject(LazyValue("VFloatType") { VFloatType }) {
    override val className = "float"

    override fun toString() = double.toString()
}

object VFloatType : VType(LazyValue("VObjectType") { VObjectType })

fun Double.toValue() = VFloat(this)

fun Float.toValue() = VFloat(this.toDouble())
