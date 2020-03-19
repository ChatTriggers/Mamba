package com.chattriggers.mamba.core.values

open class VInt(val num: Int) : VObject() {
    override val descriptor: ClassDescriptor
        get() = IntDescriptor

    override fun toString() = num.toString()
}

object IntDescriptor : ClassDescriptor(ObjectDescriptor) {
    init {
        addClassMethod("__add__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num + other.num)
        }
    }
}

fun Int.toValue() = VInt(this)
