package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.*
import java.lang.IllegalStateException
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class VComplex(val real: Double, val imag: Double) : VObject(ComplexDescriptor) {
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

object ComplexDescriptor : ClassDescriptor(ObjectDescriptor) {
    init {
        addMethodDescriptor("conjugate") {
            when (val self = argument(0)) {
                is VComplex -> VComplex(self.real, -self.imag)
                is VFloat, is VInt -> self
                else -> TODO()
            }
        }

        // Magic methods
        addMethodDescriptor("__abs__") {
            when (val self = argument(0)) {
                is VComplex -> sqrt(self.real.pow(2.0) + self.imag.pow(2.0))
                is VFloat -> self.double
                is VInt -> self.int.toDouble()
                else -> TODO()
            }.toValue()
        }
        addMethodDescriptor("__add__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> VComplex(
                    selfWide.real + otherWide.real,
                    selfWide.imag + otherWide.imag
                )
                selfWide is VFloat && otherWide is VFloat -> VFloat(selfWide.double + otherWide.double)
                selfWide is VInt && otherWide is VInt -> VInt(selfWide.int + otherWide.int)
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__bool__") {
            when (val self = argument(0)) {
                is VComplex -> if (self.real == 0.0 && self.imag == 0.0) VFalse else VTrue
                is VFloat -> if (self.double == 0.0) VFalse else VTrue
                is VInt -> if (self.int == 0) VFalse else VTrue
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__eq__") {
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
        addMethodDescriptor("__float__") {
            when (val self = argument(0)) {
                is VComplex -> TODO("TypeError")
                is VFloat -> self
                is VInt -> VFloat(self.int.toDouble())
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__floordiv__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat -> VFloat(floor(selfWide.double / otherWide.double))
                selfWide is VInt && otherWide is VInt -> VInt(selfWide.int / otherWide.int)
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__ge__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double >= otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int >= otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__gt__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double > otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int > otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__int__") {
            when (val self = argument(0)) {
                is VComplex -> TODO("TypeError")
                is VFloat -> self.double.toInt().toValue()
                is VInt -> self
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__le__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double <= otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int <= otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__lt__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double < otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int < otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__mod__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("TypeError")
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double % otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int % otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__mul__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex ->
                    VComplex(
                        selfWide.real * otherWide.real - selfWide.imag * otherWide.imag,
                        selfWide.real * otherWide.imag - selfWide.imag * otherWide.real
                    )
                selfWide is VFloat && otherWide is VFloat ->
                    (selfWide.double * otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    (selfWide.int * otherWide.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__ne__") {
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
        addMethodDescriptor("__neg__") {
            when (val self = argument(0)) {
                is VComplex -> VComplex(-self.real, -self.imag)
                is VFloat -> (-self.double.toInt()).toValue()
                is VInt -> (-self.int).toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__pos__") {
            when (val self = argument(0)) {
                is VComplex, is VFloat, is VInt -> self
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__pow__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> TODO("Not Implemented")
                selfWide is VFloat && otherWide is VFloat ->
                    selfWide.double.pow(otherWide.double).toValue()
                selfWide is VInt && otherWide is VInt ->
                    selfWide.int.toDouble().pow(otherWide.int).toInt().toValue()
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__radd__") {
            argument(1).callProperty(interp, "__add__", listOf(argument(0)))
        }
        addMethodDescriptor("__rdivmod__") {
            argument(1).callProperty(interp, "__divmod__", listOf(argument(0)))
        }
        addMethodDescriptor("__rfloordiv__") {
            argument(1).callProperty(interp, "__floordiv__", listOf(argument(0)))
        }
        addMethodDescriptor("__rmod__") {
            argument(1).callProperty(interp, "__mod__", listOf(argument(0)))
        }
        addMethodDescriptor("__rmul__") {
            argument(1).callProperty(interp, "__mul__", listOf(argument(0)))
        }
        addMethodDescriptor("__rpow__") {
            argument(1).callProperty(interp, "__pow__", listOf(argument(0)))
        }
        addMethodDescriptor("__rsub__") {
            argument(1).callProperty(interp, "__sub__", listOf(argument(0)))
        }
        addMethodDescriptor("__rtruediv__") {
            argument(1).callProperty(interp, "__truediv__", listOf(argument(0)))
        }
        addMethodDescriptor("__sub__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> VComplex(
                    selfWide.real - otherWide.real,
                    selfWide.imag - otherWide.imag
                )
                selfWide is VFloat && otherWide is VFloat -> VFloat(selfWide.double - otherWide.double)
                selfWide is VInt && otherWide is VInt -> VInt(selfWide.int - otherWide.int)
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
        addMethodDescriptor("__truediv__") {
            val (selfWide, otherWide) = widenFirstArgs()

            when {
                selfWide is VComplex && otherWide is VComplex -> VComplex(
                    selfWide.real + otherWide.real,
                    selfWide.imag + otherWide.imag
                )
                selfWide is VFloat && otherWide is VFloat -> VFloat(selfWide.double / otherWide.double)
                selfWide is VInt && otherWide is VInt -> VFloat(selfWide.int.toDouble() / otherWide.int.toDouble())
                else -> throw IllegalStateException("Expected widened numbers to have same type")
            }
        }
    }
}
