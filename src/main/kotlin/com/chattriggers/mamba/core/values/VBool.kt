package com.chattriggers.mamba.core.values

sealed class VBool(private val value: Boolean) : VInt(if (value) 1 else 0, BoolDescriptor) {
    override fun toString() = value.toString().capitalize()
}

 object BoolDescriptor : ClassDescriptor(IntDescriptor)

object VTrue : VBool(true)

object VFalse : VBool(false)

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
