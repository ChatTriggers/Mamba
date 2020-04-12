package com.chattriggers.mamba.core.modules

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VModule
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.exceptions.VValueError
import com.chattriggers.mamba.core.values.numbers.VFloat
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.unwrap

object ModuleMath : VModule("math", LazyValue("ModuleMathType") { ModuleMathType }) {
    fun fact(n: Int): Int {
        var f = 1
        for (i in 1..n) {
            f *= i
        }
        return f
    }
}

object ModuleMathType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("ceil", isStatic = true) {
            when (val x = assertArgAs<VObject>(0)) {
                is VInt -> x
                is VFloat -> x.double.toInt().toValue()
                else -> runtime.callProp(x, "__ceil__")
            }
        }
        addMethod("comb", isStatic = true) {
            val n = assertArgAs<VInt>(0) {
                return@addMethod VTypeError.construct(
                    "comb() expected 'int' type for first argument, got '${it.unwrap().className}'"
                )
            }.int
            val k = assertArgAs<VInt>(1) {
                return@addMethod VTypeError.construct(
                    "comb() expected 'int' type for second argument, got '${it.unwrap().className}'"
                )
            }.int

            if (n < 0) {
                return@addMethod VValueError.construct(
                    "k must be a non-negative integer"
                )
            } else if (k < 0) {
                return@addMethod VValueError.construct(
                    "n must be a non-negative integer"
                )
            }

            if (k > n) {
                0.toValue()
            } else {
                (ModuleMath.fact(n) / (ModuleMath.fact(k) * ModuleMath.fact(n - k))).toValue()
            }
        }
        addMethod("factorial", isStatic = true) {
            val x = assertArgAs<VInt>(0).int
            ModuleMath.fact(x).toValue()
        }
    }
}
