package com.chattriggers.mamba.core.values

sealed class VBool(val value: Boolean) : VInt(if (value) 1 else 0) {
    override val descriptor: ClassDescriptor
        get() = BoolDescriptor
}

object BoolDescriptor : ClassDescriptor(IntDescriptor)

object VTrue : VBool(true)

object VFalse : VBool(false)

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
