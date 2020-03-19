package com.chattriggers.mamba.core.values

class VString(val string: String) : VObject() {
    override val descriptor: ClassDescriptor
        get() = StringDescriptor
}

object StringDescriptor : ClassDescriptor(ObjectDescriptor) {

}

fun String.toValue() = VString(this)
