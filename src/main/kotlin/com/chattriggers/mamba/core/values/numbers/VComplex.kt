package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.singletons.VBoolType
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VTrue
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class VComplex : VObject(LazyValue("VComplexType") { VComplexType }) {
    override val className = "complex"

    var real = 0.0
    var imag = 0.0

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

    operator fun component1() = real
    operator fun component2() = imag
}

object VComplexType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            construct(VComplexType, *arguments().toTypedArray())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VComplexType>(0)
            VComplex()
        }
        addMethod("__init__") {
            val self = assertSelfAs<VComplex>()

            if (argSize > 1) {
                self.real = when (val v = argumentValueRaw(1)) {
                    is Wrapper -> v.value as Double
                    is VObject -> runtime.toDouble(v)
                    else -> TODO("Error")
                }
            }

            if (argSize > 2) {
                self.imag = when (val v = argumentValueRaw(2)) {
                    is Wrapper -> v.value as Double
                    is VObject -> runtime.toDouble(v)
                    else -> TODO("Error")
                }
            }

            VNone
        }

        addMethod("conjugate") {
            when (val self = argument(0)) {
                is VComplex -> construct(VComplexType, self.real, -self.imag)
                is VFloat, is VInt -> self
                else -> TODO()
            }
        }

        // Magic methods
        addMethod("__abs__") {
            assertSelfAs<VComplex>().let { self ->
                sqrt(self.real.pow(2.0) + self.imag.pow(2.0))
            }.let {
                construct(VFloatType, it)
            }
        }
        addMethod("__add__") {
            val (real, imag) = assertSelfAs<VComplex>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> real + other.int to imag
                is VFloat -> real + other.double to imag
                is VComplex -> real + other.real to imag + other.imag
                else -> TODO("Error")
            }.let {
                construct(VComplexType, it.first, it.second)
            }
        }
        addMethod("__bool__") {
            assertSelfAs<VComplex>().let { self ->
                if (self.real == 0.0 && self.imag == 0.0) VFalse else VTrue
            }
        }
        addMethod("__divmod__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__eq__") {
            val (real, imag) = assertSelfAs<VComplex>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> real == other.int.toDouble() && imag == 0.0
                is VFloat -> real == other.double && imag == 0.0
                is VComplex -> real == other.real && imag == other.imag
                else -> TODO("Error")
            }.let {
                construct(VBoolType, it)
            }
        }
        addMethod("__float__") {
            VTypeError.construct("can't convert complex to float")
        }
        addMethod("__floordiv__") {
            VTypeError.construct("can't take floor of complex number")
        }
        addMethod("__ge__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__gt__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__int__") {
            VTypeError.construct("can't convert complex to int")
        }
        addMethod("__le__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__lt__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__mod__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__mul__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__ne__") {
            VTypeError.construct("TODO: Error")
        }
        addMethod("__neg__") {
            val (real, imag) = assertSelfAs<VComplex>()
            construct(VComplexType, -real, -imag)
        }
        addMethod("__pos__") {
            assertSelfAs<VComplex>()
        }
        addMethod("__pow__") {
            TODO("Implement complex power algorithm")
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
        addMethod("__rpow__") {
            runtime.callProp(argument(1), "__pow__", listOf(argument(0)))
        }
        addMethod("__rsub__") {
            runtime.callProp(argument(1), "__sub__", listOf(argument(0)))
        }
        addMethod("__rtruediv__") {
            runtime.callProp(argument(1), "__truediv__", listOf(argument(0)))
        }
        addMethod("__sub__") {
            val (real, imag) = assertSelfAs<VComplex>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> real - other.int to imag
                is VFloat -> real - other.double to imag
                is VComplex -> real - other.real to imag - other.imag
                else -> TODO("Error")
            }.let {
                construct(VComplexType, it.first, it.second)
            }
        }
        addMethod("__truediv__") {
            val (real, imag) = assertSelfAs<VComplex>()

            when (val other = assertArgAs<VObject>(1)) {
                is VInt -> real / other.int to imag / other.int
                is VFloat -> real / other.double to imag / other.double
                is VComplex -> {
                    val d = other.real.pow(2.0) + other.imag.pow(2.0)
                    val r = (real * other.real + imag * other.imag) / d
                    val i = (imag * other.real - real * other.imag) / d
                    r to i
                }
                else -> TODO("Error")
            }.let {
                construct(VComplexType, it.first, it.second)
            }
        }
    }
}
