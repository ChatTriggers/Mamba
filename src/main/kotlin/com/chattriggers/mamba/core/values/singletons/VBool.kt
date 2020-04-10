package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.numbers.VIntType

sealed class VBool(val boolean: Boolean) : VInt(if (boolean) 1 else 0, LazyValue("VBoolType") { VBoolType }) {
    override val className = "bool"

    override fun toString() = boolean.toString().capitalize()
}

object VBoolType : VType(LazyValue("VIntType") { VIntType }) {
    init {
        addMethod("__call__") {
            construct(VBoolType, *arguments().toTypedArray())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VBoolType) {
                TODO()
            }

            val b = when (argSize) {
                1 -> false
                2 -> when (val arg = argumentRaw(1)) {
                    is Wrapper -> if (arg.value !is Boolean) TODO() else arg.value
                    else -> TODO()
                }
                else -> TODO()
            }

            if (b) VTrue else VFalse
        }
        addMethod("__init__") {
            VNone
        }
    }
}

object VTrue : VBool(true)

object VFalse : VBool(false)

fun Boolean.toValue() = when (this) {
    true -> VTrue
    false -> VFalse
}
