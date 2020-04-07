package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.exceptions.VArithmeticError
import com.chattriggers.mamba.core.values.exceptions.VArithmeticErrorType
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VTrue
import com.chattriggers.mamba.core.values.singletons.toValue
import java.lang.IllegalStateException
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class VComplex(val real: Double, val imag: Double) : VObject(LazyValue("VComplexType") { VComplexType }) {
    override val className = "complex"

    override fun toString(): String {
        val r = when (real) {
            0.0 -> ""
            floor(real) -> real.toInt().toString()
            else -> real.toString()
        }
        val i = when {
            real != 0.0 && imag == 0.0 -> ""
            imag == floor(imag) -> imag.toInt().toString()
            else -> imag.toString()
        }
        val op = if (imag >= 0.0 && real != 0.0) "+" else ""

        return "$r$op${i}j"
    }
}

object VComplexType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VComplexType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VComplexType) {
                notImplemented()
            }

            var real = 0.0
            var imag = 0.0

            if (argSize > 0) {
                real = when (val v = argumentRaw(1)) {
                    is Wrapper -> v.value as Double
                    is VObject -> runtime.toDouble(v)
                    else -> notImplemented("Error")
                }
            }

            if (argSize > 1) {
                imag = when (val v = argumentRaw(2)) {
                    is Wrapper -> v.value as Double
                    is VObject -> runtime.toDouble(v)
                    else -> notImplemented("Error")
                }
            }

            VComplex(real, imag)
        }
        addMethod("__init__") {
            VNone
        }

        addMethod("conjugate") {
            when (val self = argument(0)) {
                is VComplex -> runtime.construct(VComplexType, listOf(Wrapper(self.real), Wrapper(-self.imag)))
                is VFloat, is VInt -> self
                else -> notImplemented()
            }
        }

        // Magic methods
        addMethod("__abs__") {
            when (val self = argument(0)) {
                is VComplex -> sqrt(self.real.pow(2.0) + self.imag.pow(2.0))
                is VFloat -> self.double
                is VInt -> self.int.toDouble()
                else -> notImplemented()
            }.toValue()
        }
        addMethod("__add__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> runtime.construct(VComplexType, listOf(
                    Wrapper(selfWide.real + otherWide.real),
                    Wrapper(selfWide.imag + otherWide.imag)
                ))
                selfWide is VFloat && otherWide is VFloat -> runtime.construct(VFloatType, listOf(
                    Wrapper(selfWide.double + otherWide.double)
                ))
                selfWide is VInt && otherWide is VInt -> runtime.construct(VIntType, listOf(
                    Wrapper(selfWide.int + otherWide.int)
                ))
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__bool__") {
            when (val self = argument(0)) {
                is VComplex -> if (self.real == 0.0 && self.imag == 0.0) VFalse else VTrue
                is VFloat -> if (self.double == 0.0) VFalse else VTrue
                is VInt -> if (self.int == 0) VFalse else VTrue
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
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
        addMethod("__float__") {
            when (val self = argument(0)) {
                is VComplex -> notImplemented("TypeError")
                is VFloat -> self
                is VInt -> runtime.construct(VFloatType, listOf(Wrapper(self.int.toDouble())))
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__floordiv__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat -> runtime.construct(VFloatType, listOf(
                    Wrapper(floor(selfWide.double / otherWide.double))
                ))
                selfWide is VInt && otherWide is VInt -> runtime.construct(VIntType, listOf(
                    Wrapper(selfWide.int / otherWide.int)
                ))
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__ge__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double >= otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int >= otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__gt__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double > otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int > otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__int__") {
            when (val self = argument(0)) {
                is VComplex -> notImplemented("TypeError")
                is VFloat -> self.double.toInt().toValue()
                is VInt -> self
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__le__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double <= otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int <= otherWide.int).toValue()
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
        addMethod("__mod__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double % otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int % otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__mul__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> runtime.construct(VComplexType, listOf(
                    Wrapper(selfWide.real * otherWide.real - selfWide.imag * otherWide.imag),
                    Wrapper(selfWide.real * otherWide.imag - selfWide.imag * otherWide.real)
                ))
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double * otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int * otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__ne__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex ->
                    (selfWide.real != otherWide.real || selfWide.imag != otherWide.imag).toValue()
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double != otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int != otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__neg__") {
            when (val self = argument(0)) {
                is VComplex -> runtime.construct(VComplexType, listOf(
                    Wrapper(-self.real),
                    Wrapper(-self.imag)
                ))
                is VFloat -> (-self.double.toInt()).toValue()
                is VInt -> (-self.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__pos__") {
            when (val self = argument(0)) {
                is VComplex, is VFloat, is VInt -> self
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__pow__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> notImplemented("Not Implemented")
                selfWide is VFloat && otherWide is VFloat ->
                    selfWide.double.pow(otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    selfWide.int.toDouble().pow(otherWide.int).toInt().toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__radd__") {
            runtime.callProperty(argument(1), "__add__", listOf(argument(0)))
        }
        addMethod("__rdivmod__") {
            runtime.callProperty(argument(1), "__divmod__", listOf(argument(0)))
        }
        addMethod("__rfloordiv__") {
            runtime.callProperty(argument(1), "__floordiv__", listOf(argument(0)))
        }
        addMethod("__rmod__") {
            runtime.callProperty(argument(1), "__mod__", listOf(argument(0)))
        }
        addMethod("__rmul__") {
            runtime.callProperty(argument(1), "__mul__", listOf(argument(0)))
        }
        addMethod("__rpow__") {
            runtime.callProperty(argument(1), "__pow__", listOf(argument(0)))
        }
        addMethod("__rsub__") {
            runtime.callProperty(argument(1), "__sub__", listOf(argument(0)))
        }
        addMethod("__rtruediv__") {
            runtime.callProperty(argument(1), "__truediv__", listOf(argument(0)))
        }
        addMethod("__sub__", id = "complex_sub") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> runtime.construct(VComplexType, listOf(
                    Wrapper(selfWide.real - otherWide.real),
                    Wrapper(selfWide.imag - otherWide.imag)
                ))
                selfWide is VFloat && otherWide is VFloat -> runtime.construct(VFloatType, listOf(
                    Wrapper(selfWide.double - otherWide.double)
                ))
                selfWide is VInt && otherWide is VInt -> runtime.construct(VIntType, listOf(
                    Wrapper(selfWide.int - otherWide.int)
                ))
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethod("__truediv__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> runtime.construct(VComplexType, listOf(
                    Wrapper(selfWide.real + otherWide.real),
                    Wrapper(selfWide.imag + otherWide.imag)
                ))
                selfWide is VFloat && otherWide is VFloat -> runtime.construct(VFloatType, listOf(
                    Wrapper(selfWide.double / otherWide.double)
                ))
                selfWide is VInt && otherWide is VInt -> runtime.construct(VFloatType, listOf(
                    Wrapper(selfWide.int.toDouble() / otherWide.int.toDouble())
                ))
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
    }
}
