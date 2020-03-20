package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.ClassDescriptor
import com.chattriggers.mamba.core.values.VObject
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.ln

open class VInt(val int: Int, descriptor: ClassDescriptor = IntDescriptor) : VObject(descriptor) {
    override fun toString() = int.toString()
}

object IntDescriptor : ClassDescriptor(ComplexDescriptor) {
    init {
        addClassMethod("bit_length") {
            val self = assertSelf<VInt>()
            ceil(ln(self.int.toDouble()) / ln(2.toDouble())).toInt().toValue()
        }

        // Magic methods
        addClassMethod("__and__") {
            val self = assertSelf<VInt>()
            val other = assertArg<VInt>(1)

            VInt(self.int and other.int)
        }
        addClassMethod("__ceil__") {
            argument(0)
        }
        addClassMethod("__floor__") {
            argument(0)
        }
        addClassMethod("__invert__") {
            val self = assertSelf<VInt>()
            VInt(self.int.inv())
        }
        addClassMethod("__lshift__") {
            val self = assertSelf<VInt>()
            val other = assertArg<VInt>(1)
            VInt(self.int shl other.int)
        }
        addClassMethod("__rand__") {
            argument(1).callProperty(interp, "__and__", listOf(argument(0)))
        }
        addClassMethod("__rlshift__") {
            argument(1).callProperty(interp, "__lshift__", listOf(argument(0)))
        }
        addClassMethod("__rrshift__") {
            argument(1).callProperty(interp, "__rshift__", listOf(argument(0)))
        }
        addClassMethod("__rshift__") {
            val self = assertSelf<VInt>()
            val other = assertArg<VInt>(1)
            VInt(self.int shr other.int)
        }
        addClassMethod("__ror__") {
            argument(1).callProperty(interp, "__or__", listOf(argument(0)))
        }
        addClassMethod("__rxor__") {
            argument(1).callProperty(interp, "__xor__", listOf(argument(0)))
        }
        addClassMethod("__or__") {
            val self = assertSelf<VInt>()
            val other = assertArg<VInt>(1)

            VInt(self.int or other.int)
        }
        addClassMethod("__xor__") {
            val self = assertSelf<VInt>()
            val other = assertArg<VInt>(1)

            VInt(self.int xor other.int)
        }
    }
}

fun Int.toValue() = VInt(this)
