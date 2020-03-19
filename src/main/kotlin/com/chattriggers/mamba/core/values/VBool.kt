package com.chattriggers.mamba.core.values

sealed class VBool(value: Boolean) : VInt(if (value) 1 else 0, BoolDescriptor)

 object BoolDescriptor : ClassDescriptor(IntDescriptor)

object VTrue : VBool(true)

object VFalse : VBool(false)

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
