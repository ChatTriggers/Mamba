package com.chattriggers.mamba.core.values

class VString(val string: String) : VObject(StringDescriptor) {
    override fun toString() = "'$string'"
}

object StringDescriptor : ClassDescriptor(ObjectDescriptor) {
    init {
        addClassMethod("__add__") {
            val self = assertSelf<VString>()
            val other = assertArg<VString>(1)

            (self.string + other.string).toValue()
        }
        addClassMethod("lower") {
            assertSelf<VString>().string.toLowerCase().toValue()
        }
    }
}

fun String.toValue() = VString(this)
