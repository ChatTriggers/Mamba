package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.VObject

open class VInt(val num: Int) : VObject() {
    override val className: String
        get() = "int"

    init {
        addNativeMethod("__neg__") { _, args ->
            val self = assertSelf<VInt>(args)
            VInt(-self.num)
        }

        addNativeMethod("__pos__") { _, args ->
            val self = assertSelf<VInt>(args)
            VInt(+self.num)
        }
    }

    override fun toString() = num.toString()
}

fun Int.toValue() = VInt(this)
