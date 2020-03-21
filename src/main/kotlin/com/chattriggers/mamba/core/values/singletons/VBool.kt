package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.numbers.VIntType

sealed class VBool(private val value: Boolean) : VInt(if (value) 1 else 0, LazyValue("VBoolType") { VBoolType }) {
    override val className = "bool"

    override fun toString() = value.toString().capitalize()
}

object VBoolType : VType(LazyValue("VIntType") { VIntType })

object VTrue : VBool(true)

object VFalse : VBool(false)

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
