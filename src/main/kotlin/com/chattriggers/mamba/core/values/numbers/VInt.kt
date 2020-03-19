package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.VObject
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

open class VInt(internal val num: Int) : VObject() {
    override val className: String
        get() = "int"

    init {
        addNativeMethod("__abs__") { _, args ->
            val self = assertSelf<VInt>(args)
            abs(self.num).toValue()
        }
        addNativeMethod("__add__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num + other.num).toValue()
        }
        addNativeMethod("__sub__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num - other.num).toValue()
        }
        addNativeMethod("__mul__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num * other.num).toValue()
        }
        addNativeMethod("__truediv__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num.toDouble() / other.num.toDouble()).toValue()
        }
        addNativeMethod("__floordiv__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num / other.num).toValue()
        }
        addNativeMethod("__mod__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num % other.num).toValue()
        }
        addNativeMethod("__pow__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            self.num.toDouble().pow(other.num).toValue()
        }
        addNativeMethod("__and__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num and other.num).toValue()
        }
        addNativeMethod("__or__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num and other.num).toValue()
        }
        addNativeMethod("__xor__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num and other.num).toValue()
        }
        addNativeMethod("__lshift__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num shl other.num).toValue()
        }
        addNativeMethod("__rshift__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num shr other.num).toValue()
        }
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
