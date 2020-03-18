package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VString
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.numbers.VInt

class Runtime(val interp: Interpreter) {
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