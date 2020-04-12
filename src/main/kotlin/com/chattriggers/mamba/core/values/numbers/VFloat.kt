package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.collections.VStringType
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.singletons.VBoolType
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VTrue
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.pow

class VFloat(val double: Double) : VObject(LazyValue("VFloatType") { VFloatType }) {
    override val className = "float"

    override fun toString() = double.toString()
}

object VFloatType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            construct(VFloatType, *arguments().toTypedArray())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VFloatType) {
                TODO()
            }

            var num = 0.0

            if (argSize > 0) {
                num = when (val v = argumentValueRaw(1)) {
                    is VObject -> runtime.toDouble(v)
                    is Wrapper -> when (val wrapped = v.value) {
                        is String -> java.lang.Double.parseDouble(wrapped)
                        is Double -> wrapped
                        else -> TODO("Error")
                    }
                    else -> TODO("Error")
                }
            }

            VFloat(num)
        }
        addMethod("__init__") {
            VNone
        }

        addMethod("__abs__") {
            construct(VFloatType, assertSelfAs<VFloat>().double.absoluteValue)
        }
        addMethod("__add__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VFloatType, self.double + other.int.toDouble())
                is VFloat -> construct(VFloatType, self.double + other.double)
                is VComplex -> construct(VComplexType, self.double + other.real, other.imag)
                else -> TODO("Error")
            }
        }
        addMethod("__bool__") {
            if (assertSelfAs<VFloat>().double == 0.0) VFalse else VTrue
        }
        addMethod("__divmod__") {
            TODO()
        }
        addMethod("__eq__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double == other.int.toDouble())
                is VFloat -> construct(VBoolType, self.double == other.double)
                is VComplex -> construct(VBoolType, self.double == other.real && other.imag == 0.0)
                else -> TODO("Error")
            }
        }
        addMethod("__float__") {
            assertSelfAs<VFloat>()
        }
        addMethod("__floordiv__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VFloatType, floor(self.double / other.int.toDouble()))
                is VFloat -> construct(VFloatType, floor((self.double / other.double)))
                is VComplex -> {
                    val d = other.real.pow(2.0) + other.imag.pow(2.0)
                    val r = floor((self.double * other.real) / d)
                    val i = floor(-(self.double * other.imag) / d)
                    construct(VComplexType, r, i)
                }
                else -> TODO("Error")
            }
        }
        addMethod("__ge__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double >= other.int.toDouble())
                is VFloat -> construct(VBoolType, self.double >= other.double)
                else -> TODO()
            }
        }
        addMethod("__gt__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double > other.int)
                is VFloat -> construct(VBoolType, self.double > other.double)
                else -> TODO()
            }
        }
        addMethod("__int__") {
            construct(VIntType, assertSelfAs<VFloat>().double.toInt())
        }
        addMethod("__le__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double <= other.int.toDouble())
                is VFloat -> construct(VBoolType, self.double <= other.double)
                else -> TODO()
            }
        }
        addMethod("__lt__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double < other.int.toDouble())
                is VFloat -> construct(VBoolType, self.double < other.double)
                else -> TODO()
            }
        }
        addMethod("__mod__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.double.rem(other.int).toInt())
                is VFloat -> construct(VFloatType, self.double.rem(other.double))
                else -> TODO()
            }
        }
        addMethod("__mul__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, (self.double * other.int).toInt())
                is VFloat -> construct(VFloatType, self.double * other.double)
                is VComplex -> construct(VComplexType, self.double * other.real, self.double * other.imag)
                else -> TODO()
            }
        }
        addMethod("__ne__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VBoolType, self.double != other.int.toDouble())
                is VFloat -> construct(VBoolType, self.double != other.double)
                is VComplex -> construct(VBoolType, self.double != other.real || other.imag == 0.0)
                else -> TODO("Error")
            }
        }
        addMethod("__neg__") {
            construct(VFloatType, -assertSelfAs<VFloat>().double)
        }
        addMethod("__pow__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, self.double.pow(other.int).toInt())
                is VFloat -> construct(VFloatType, self.double.pow(other.double))
                is VComplex -> TODO("TODO")
                else -> TODO()
            }
        }
        addMethod("__radd__") {
            runtime.callProp(argument(1), "__add__", listOf(argument(0)))
        }
        addMethod("__rdivmod__") {
            runtime.callProp(argument(1), "__divmod__", listOf(argument(0)))
        }
        addMethod("__rfloordiv__") {
            runtime.callProp(argument(1), "__floordiv__", listOf(argument(0)))
        }
        addMethod("__rmod__") {
            runtime.callProp(argument(1), "__mod__", listOf(argument(0)))
        }
        addMethod("__rmul__") {
            runtime.callProp(argument(1), "__mul__", listOf(argument(0)))
        }
        addMethod("__round__") {
            TODO()
        }
        addMethod("__rpow__") {
            runtime.callProp(argument(1), "__pow__", listOf(argument(0)))
        }
        addMethod("__rsub__") {
            runtime.callProp(argument(1), "__sub__", listOf(argument(0)))
        }
        addMethod("__rtruediv__") {
            runtime.callProp(argument(1), "__truediv__", listOf(argument(0)))
        }
        addMethod("__str__") {
            construct(VStringType, assertSelfAs<VFloat>().double.toString())
        }
        addMethod("__sub__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VIntType, (self.double - other.int.toDouble()).toInt())
                is VFloat -> construct(VFloatType, self.double - other.double)
                is VComplex -> construct(VComplexType, self.double - other.real, -other.imag)
                else -> TODO("Error")
            }
        }
        addMethod("__truediv__") {
            val self = assertSelfAs<VFloat>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> construct(VFloatType, self.double / other.int.toDouble())
                is VFloat -> construct(VFloatType, self.double / other.double)
                is VComplex -> {
                    val d = other.real.pow(2.0) + other.imag.pow(2.0)
                    val r = (self.double * other.real) / d
                    val i = -(self.double * other.imag) / d
                    construct(VComplexType, r, i)
                }
                else -> TODO()
            }
        }
    }
}
