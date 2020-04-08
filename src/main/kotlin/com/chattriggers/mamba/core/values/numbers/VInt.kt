package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VStringType
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.*
import java.lang.Math.abs
import kotlin.math.*

open class VInt(val int: Int, type: LazyValue<VType> = LazyValue("VIntType") { VIntType }) : VObject(type) {
    override val className = "int"

    override fun toString() = int.toString()
}

object VIntType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            construct(VIntType, *arguments().toTypedArray())
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

        addMethod("bit_length") {
            val self = assertSelfAs<VInt>()
            ceil(ln(self.int.toDouble()) / ln(2.toDouble())).toInt().toValue()
        }

        addMethod("__abs__") {
            construct(VIntType, assertSelfAs<VInt>().int.absoluteValue)
        }
        addMethod("__add__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int + other.int)
                is VFloat -> construct(VFloatType, self.int.toDouble() + other.double)
                is VComplex -> construct(VComplexType, self.int.toDouble() + other.real, other.imag)
                else -> notImplemented("Error")
            }
        }
        addMethod("__and__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)

            construct(VIntType, self.int and other.int)
        }
        addMethod("__bool__") {
            if (assertSelfAs<VInt>().int == 0) VFalse else VTrue
        }
        addMethod("__ceil__") {
            argument(0)
        }
        addMethod("__divmod__") {
            notImplemented()
        }
        addMethod("__eq__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int == other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() == other.double)
                is VComplex -> construct(VBoolType, self.int.toDouble() == other.real && other.imag == 0.0)
                else -> notImplemented("Error")
            }
        }
        addMethod("__float__") {
            val self = assertSelfAs<VInt>()
            construct(VFloatType, self.int.toDouble())
        }
        addMethod("__floor__") {
            assertSelfAs<VInt>()
        }
        addMethod("__floordiv") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int / other.int)
                is VFloat -> construct(VFloatType, floor((self.int.toDouble() / other.double)))
                is VComplex -> {
                    val d = other.real.pow(2.0) + other.imag.pow(2.0)
                    val r = floor((self.int.toDouble() * other.real) / d)
                    val i = floor(-(self.int.toDouble() * other.imag) / d)
                    construct(VComplexType, r, i)
                }
                else -> notImplemented("Error")
            }
        }
        addMethod("__ge__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int >= other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() >= other.double)
                else -> notImplemented()
            }
        }
        addMethod("__gt__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int > other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() > other.double)
                else -> notImplemented()
            }
        }
        addMethod("__int__") {
            assertSelfAs<VInt>()
        }
        addMethod("__invert__") {
            construct(VIntType, assertSelfAs<VInt>().int.inv())
        }
        addMethod("__le__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int <= other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() <= other.double)
                else -> notImplemented()
            }
        }
        addMethod("__lshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            construct(VIntType, self.int shl other.int)
        }
        addMethod("__lt__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int < other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() < other.double)
                else -> notImplemented()
            }
        }
        addMethod("__mod__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int.rem(other.int))
                is VFloat -> construct(VFloatType, self.int.toDouble().rem(other.double))
                else -> notImplemented()
            }
        }
        addMethod("__mul__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int * other.int)
                is VFloat -> construct(VFloatType, self.int.toDouble() * other.double)
                is VComplex -> construct(VComplexType, self.int.toDouble() * other.real, self.int.toDouble() * other.imag)
                else -> notImplemented()
            }
        }
        addMethod("__ne__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.int != other.int)
                is VFloat -> construct(VBoolType, self.int.toDouble() != other.double)
                is VComplex -> construct(VBoolType, self.int.toDouble() != other.real || other.imag == 0.0)
                else -> notImplemented("Error")
            }
        }
        addMethod("__neg__") {
            construct(VIntType, -assertSelfAs<VInt>().int)
        }
        addMethod("__or__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            construct(VIntType, self.int or other.int)
        }
        addMethod("__pow__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int.toDouble().pow(other.int).toInt())
                is VFloat -> construct(VFloatType, self.int.toDouble().pow(other.double))
                is VComplex -> notImplemented("TODO")
                else -> notImplemented()
            }
        }
        addMethod("__radd__") {
            runtime.callProperty(argument(1), "__add__", listOf(argument(0)))
        }
        addMethod("__rand__") {
            runtime.callProperty(argument(1), "__and__", listOf(argument(0)))
        }
        addMethod("__rdivmod__") {
            runtime.callProperty(argument(1), "__divmod__", listOf(argument(0)))
        }
        addMethod("__rfloordiv__") {
            runtime.callProperty(argument(1), "__floordiv__", listOf(argument(0)))
        }
        addMethod("__rlshift__") {
            runtime.callProperty(argument(1), "__lshift__", listOf(argument(0)))
        }
        addMethod("__rmod__") {
            runtime.callProperty(argument(1), "__mod__", listOf(argument(0)))
        }
        addMethod("__rmul__") {
            runtime.callProperty(argument(1), "__mul__", listOf(argument(0)))
        }
        addMethod("__ror__") {
            runtime.callProperty(argument(1), "__or__", listOf(argument(0)))
        }
        addMethod("__round__") {
            notImplemented()
        }
        addMethod("__rpow__") {
            runtime.callProperty(argument(1), "__pow__", listOf(argument(0)))
        }
        addMethod("__rrshift__") {
            runtime.callProperty(argument(1), "__rshift__", listOf(argument(0)))
        }
        addMethod("__rshift__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            construct(VIntType, self.int shr other.int)
        }
        addMethod("__rsub__") {
            runtime.callProperty(argument(1), "__sub__", listOf(argument(0)))
        }
        addMethod("__rtruediv__") {
            runtime.callProperty(argument(1), "__truediv__", listOf(argument(0)))
        }
        addMethod("__rxor__") {
            runtime.callProperty(argument(1), "__xor__", listOf(argument(0)))
        }
        addMethod("__str__") {
            construct(VStringType, assertSelfAs<VInt>().int.toString())
        }
        addMethod("__sub__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.int - other.int)
                is VFloat -> construct(VFloatType, self.int.toDouble() - other.double)
                is VComplex -> construct(VComplexType, self.int.toDouble() - other.real, -other.imag)
                else -> notImplemented("Error")
            }
        }
        addMethod("__truediv__") {
            val self = assertSelfAs<VInt>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VFloatType, self.int.toDouble() / other.int.toDouble())
                is VFloat -> construct(VFloatType, self.int.toDouble() / other.double)
                is VComplex -> {
                    val d = other.real.pow(2.0) + other.imag.pow(2.0)
                    val r = (self.int.toDouble() * other.real) / d
                    val i = -(self.int.toDouble() * other.imag) / d
                    construct(VComplexType, r, i)
                }
                else -> notImplemented()
            }
        }
        addMethod("__xor__") {
            val self = assertSelfAs<VInt>()
            val other = assertArgAs<VInt>(1)
            construct(VIntType, self.int xor other.int)
        }
    }
}

fun Int.toValue() = VInt(this)
