package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.functions.IMethod
import com.chattriggers.mamba.core.values.numbers.*
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VNotImplemented
import com.chattriggers.mamba.core.values.singletons.toValue

class Runtime(val interp: Interpreter) {
    fun toBoolean(value: VObject): Boolean {
        return when {
            value is VNone || value is VFalse -> false
            value is VInt -> value.int != 0
            value is VString -> value.string.isNotEmpty()
            value is VList -> value.list.isNotEmpty()
            value is VDict -> value.dict.isNotEmpty()
            "__bool__" in value -> toBoolean(value.callProperty(interp, "__bool__"))
            "__len__" in value -> toInt(value.callProperty(interp, "__len__")) != 0
            else -> true
        }

    }

    fun toInt(value: VObject): Int {
        TODO()
    }

    fun widen(left: VObject, right: VObject): Pair<VObject, VObject> {
        return widenHelper(left, right) to widenHelper(right, left)
    }

    private fun widenHelper(self: VObject, other: VObject): VObject {
        if (self !is VInt && self !is VFloat && self !is VComplex)
            TODO()

        if (other !is VInt && other !is VFloat && other !is VComplex)
            TODO()

        return when (other) {
            is VComplex -> when (self) {
                is VComplex -> self
                is VFloat -> VComplex(
                    self.double,
                    0.0
                )
                is VInt -> VComplex(
                    self.int.toDouble(),
                    0.0
                )
                else -> TODO()
            }
            is VFloat -> when (self) {
                is VComplex, is VFloat -> self
                is VInt -> VFloat(
                    self.int.toDouble()
                )
                else -> TODO()
            }
            is VInt -> self
            else -> TODO()
        }
    }

    fun call(obj: VObject, args: List<VObject>): VObject {
        if (obj is ICallable)
            return obj.call(interp, args)

        return obj.callProperty(interp, "__call__", args)
    }

    fun valueCompare(method: String, left: VObject, right: VObject): VObject {
        return when (method) {
            in left -> left.callProperty(interp, method, listOf(right))
            else -> VNotImplemented
        }
    }

    fun valueArithmetic(method: String, reverseMethod: String, left: VObject, right: VObject): VObject {
        return when {
            method in left -> left.callProperty(interp, method, listOf(right))
            reverseMethod in right -> right.callProperty(interp, reverseMethod, listOf(left))
            else -> VNotImplemented
        }
    }

    fun dir(args: List<VObject>): VObject {
        if (args.isEmpty() || args.size > 2)
            TODO()

        return args[0].callProperty(interp, "__dir__", listOf(args[0]))
    }

    companion object {
        fun toValue(obj: Any): VObject {
            return when (obj) {
                is Int -> obj.toValue()
                is String -> obj.toValue()
                is Boolean -> obj.toValue()
                else -> throw IllegalArgumentException()
            }
        }
    }
}
