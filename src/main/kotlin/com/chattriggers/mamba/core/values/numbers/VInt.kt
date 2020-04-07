package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.toValue
import kotlin.math.ceil
import kotlin.math.ln

open class VInt(val int: Int, type: LazyValue<VType> = LazyValue("VIntType") { VIntType }) : VObject(type) {
    override val className = "int"

    override fun toString() = int.toString()
}

object VIntType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("bit_length") {
            val self = assertSelfAs<VInt>()
            ceil(ln(self.int.toDouble()) / ln(2.toDouble())).toInt().toValue()
        }

        addMethod("__and__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)

            runtime.construct(VIntType, listOf(Wrapper(self.int and other.int)))
        }
        addMethod("__ceil__") {
            argument(0)
        }
        addMethod("__floor__") {
            argument(0)
        }
        addMethod("__invert__") {
            val self = assertSelfAs<VInt>()
            runtime.construct(VIntType, listOf(Wrapper(self.int.inv())))
        }
        addMethod("__lshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int shl other.int)))
        }
        addMethod("__neg__") {
            runtime.construct(VIntType, listOf(Wrapper(-assertSelfAs<VInt>().int)))
        }
        addMethod("__rand__") {
            runtime.callProperty(argument(1), "__and__", listOf(argument(0)))
        }
        addMethod("__rlshift__") {
            runtime.callProperty(argument(1), "__lshift__", listOf(argument(0)))
        }
        addMethod("__rrshift__") {
            runtime.callProperty(argument(1), "__rshift__", listOf(argument(0)))
        }
        addMethod("__rshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int shr other.int)))
        }
        addMethod("__ror__") {
            runtime.callProperty(argument(1), "__or__", listOf(argument(0)))
        }
        addMethod("__rxor__") {
            runtime.callProperty(argument(1), "__xor__", listOf(argument(0)))
        }
        addMethod("__or__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int or other.int)))
        }
        addMethod("__add__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int + other.int)))
        }
        addMethod("__sub__", id = "int_sub") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int - other.int)))
        }
        addMethod("__xor__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            runtime.construct(VIntType, listOf(Wrapper(self.int xor other.int)))
        }
        addMethod("__call__") {
            runtime.construct(VIntType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VIntType) {
                notImplemented()
            }

            var num = 0

            if (argSize > 0) {
                num = when (val v = argumentRaw(1)) {
                    is Wrapper -> v.value as Int
                    is VObject -> runtime.toInt(v)
                    else -> notImplemented("Error")
                }
            }

            VInt(num)
        }
        addMethod("__init__") {
            VNone
        }
        addMethod("__eq__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex ->
                    (selfWide.real == otherWide.real && selfWide.imag == otherWide.imag).toValue()
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double == otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int == otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__lt__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double < otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int < otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
    }
}

fun Int.toValue() = VInt(this)
