package com.chattriggers.mamba.core.values

import kotlin.math.abs
import kotlin.math.ceil

open class VInt(val num: Int, descriptor: ClassDescriptor = IntDescriptor) : VObject(descriptor) {
    override fun toString() = num.toString()
}

object IntDescriptor : ClassDescriptor(ObjectDescriptor) {
    init {
        addClassMethod("__abs__") {_, args ->
            val self = assertSelf<VInt>(args)
            abs(self.num).toValue()
        }
        addClassMethod("__add__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num + other.num)
        }
        addClassMethod("__and__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num and other.num)
        }
        addClassMethod("__lt__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num < other.num).toValue()
        }
        addClassMethod("__lte__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num <= other.num).toValue()
        }
        addClassMethod("__gt__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num > other.num).toValue()
        }
        addClassMethod("__gte__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num >= other.num).toValue()
        }
        addClassMethod("__eq__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num == other.num).toValue()
        }
        addClassMethod("__lshift__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num shl other.num)
        }
        addClassMethod("__ne__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            (self.num != other.num).toValue()
        }
        addClassMethod("__or__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num or other.num)
        }
        addClassMethod("__rshift__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num shr other.num)
        }
        addClassMethod("__sub__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num - other.num)
        }
        addClassMethod("__xor__") { _, args ->
            val self = assertSelf<VInt>(args)
            val other = assertArg<VInt>(args, 1)
            VInt(self.num xor other.num)
        }
    }
}

fun Int.toValue() = VInt(this)
