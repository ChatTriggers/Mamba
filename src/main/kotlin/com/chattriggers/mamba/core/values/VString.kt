package com.chattriggers.mamba.core.values

class VString(val string: String) : VObject(StringDescriptor) {
    override fun toString() = "'$string'"
}

object StringDescriptor : ClassDescriptor(ObjectDescriptor)

fun String.toValue() = VString(this)
