package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.numbers.VFalse
import com.chattriggers.mamba.core.values.numbers.VInt

class Runtime(val interp: Interpreter) {
    fun toBoolean(value: Value): Boolean {
        return when {
            value is VNone || value is VFalse -> false
            value is VInt -> value.num != 0
            value is VString -> value.string.isNotEmpty()
            value is VList<*> -> value.list.isNotEmpty()
            value is VDict<*> -> value.map.isNotEmpty()
            "__bool__" in value -> toBoolean(value.callFunction(interp, "__bool__"))
            "__len__" in value -> toInt(value.callFunction(interp, "__len__")) != 0
            else -> true
        }

    }

    fun toInt(value: Value): Int {
        TODO()
    }

    fun add(left: Value, right: Value): Value {
        when {
            left is VInt && right is VInt -> return VInt(left.num + right.num)
        }

        return VNone
    }

    fun dir(args: List<Value>): Value {
        if (args.isEmpty() || args.size > 2)
            TODO()

        return args[0].callFunction(interp, "__dir__")
    }
}
