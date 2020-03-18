package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.VNativeFunction
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType

class VInt(val num: Int) : VObject() {
    init {
        inherit(TYPE)
    }

    override fun toString() = num.toString()

    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "int"

            init {
                // TODO: These should be methods
                slots["__neg__"] = VNativeFunction("__neg__") { _, args ->
                    val self = assertSelf<VInt>(args)
                    VInt(-self.num)
                }
                slots["__pos__"] = VNativeFunction("__pos__") { _, args ->
                    val self = assertSelf<VInt>(args)
                    VInt(+self.num)
                }
            }
        }
    }
}

fun Int.toValue() = VInt(this)
