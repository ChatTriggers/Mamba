package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType
import kotlin.math.ceil
import kotlin.math.ln

open class VInt(val int: Int, type: LazyValue<VType> = LazyValue("VIntType") { VIntType }) : VObject(type) {
    override val className = "int"

    override fun toString() = int.toString()
}

object VIntType : VType(LazyValue("VComplexType") { VComplexType }) {
    init {
        addMethodDescriptor("bit_length") {
            val self = assertSelfAs<VInt>()
            ceil(ln(self.int.toDouble()) / ln(2.toDouble())).toInt().toValue()
        }

        // Magic methods
        addMethodDescriptor("__and__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)

            VInt(self.int and other.int)
        }
        addMethodDescriptor("__ceil__") {
            argument(0)
        }
        addMethodDescriptor("__floor__") {
            argument(0)
        }
        addMethodDescriptor("__invert__") {
            val self = assertSelfAs<VInt>()
            VInt(self.int.inv())
        }
        addMethodDescriptor("__lshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            VInt(self.int shl other.int)
        }
        addMethodDescriptor("__rand__") {
            argument(1).callProperty(interp, "__and__", listOf(argument(0)))
        }
        addMethodDescriptor("__rlshift__") {
            argument(1).callProperty(interp, "__lshift__", listOf(argument(0)))
        }
        addMethodDescriptor("__rrshift__") {
            argument(1).callProperty(interp, "__rshift__", listOf(argument(0)))
        }
        addMethodDescriptor("__rshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            VInt(self.int shr other.int)
        }
        addMethodDescriptor("__ror__") {
            argument(1).callProperty(interp, "__or__", listOf(argument(0)))
        }
        addMethodDescriptor("__rxor__") {
            argument(1).callProperty(interp, "__xor__", listOf(argument(0)))
        }
        addMethodDescriptor("__or__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)

            VInt(self.int or other.int)
        }
        addMethodDescriptor("__xor__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)

            VInt(self.int xor other.int)
        }
    }
}

fun Int.toValue() = VInt(this)
