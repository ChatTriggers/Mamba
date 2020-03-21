package com.chattriggers.mamba.core.values

class VString(val string: String) : VObject(LazyValue("VStringType") { VStringType }) {
    override val className = "str"

    override fun toString() = "'$string'"
}

object VStringType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__add__") {
            val self = assertSelf<VString>()
            val other = assertArg<VString>(1)

            (self.string + other.string).toValue()
        }
        addMethodDescriptor("__call__") {
            assertArg<VObject>(0).callProperty(interp, "__str__")
        }
        addMethodDescriptor("lower") {
            assertSelf<VString>().string.toLowerCase().toValue()
        }
    }
}

fun String.toValue() = VString(this)
