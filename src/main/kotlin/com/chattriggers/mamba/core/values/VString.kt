package com.chattriggers.mamba.core.values

class VString(val string: String) : VObject(LazyValue("VStringType") { VStringType }) {
    override val className = "str"

    override fun toString() = "'$string'"
}

object VStringType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__add__") {
            val self = assertSelfAs<VString>()
            val other = assertArgAs<VString>(1)

            (self.string + other.string).toValue()
        }
        addMethodDescriptor("__call__") {
            assertArgAs<VObject>(0).callProperty(interp, "__str__")
        }
        addMethodDescriptor("lower") {
            assertSelfAs<VString>().string.toLowerCase().toValue()
        }
    }
}

fun String.toValue() = VString(this)
