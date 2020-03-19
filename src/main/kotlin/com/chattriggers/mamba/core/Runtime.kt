package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VList

class Runtime(val interp: Interpreter) {
    fun toBoolean(value: VObject): Boolean {
        return when {
            value is VNone || value is VFalse -> false
            value is VInt -> value.num != 0
            value is VString -> value.string.isNotEmpty()
            value is VList<*> -> value.list.isNotEmpty()
            value is VDict<*, *> -> value.dict.isNotEmpty()
            "__bool__" in value -> toBoolean(value.callProperty(interp, "__bool__"))
            "__len__" in value -> toInt(value.callProperty(interp, "__len__")) != 0
            else -> true
        }

    }

    fun toInt(value: VObject): Int {
        TODO()
    }

    fun valueCompare(method: String, left: VObject, right: VObject): VObject {
        return when (method) {
            in left -> left.callProperty(interp, method, listOf(right))
            else -> VNotImplemented
        }
    }

    fun valueArithmetic(method: String, reverseMethod: String, left: VObject, right: VObject): VObject {
        return when {
            left.contains(method) -> left.callProperty(interp, method, listOf(right))
            right.contains(reverseMethod) -> right.callProperty(interp, reverseMethod, listOf(left)) // TODO: Only if types differ
            else -> VNotImplemented
        }
    }

    fun dir(args: List<VObject>): VObject {
        if (args.isEmpty() || args.size > 2)
            TODO()

        return args[0].callProperty(interp, "__dir__")
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
