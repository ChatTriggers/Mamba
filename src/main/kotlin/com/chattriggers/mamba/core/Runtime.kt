package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.numbers.VInt

object Runtime {
    fun add(left: Value, right: Value): Value {
        when {
            left is VInt && right is VInt -> return VInt(left.num + right.num)
        }

        return VNone
    }
}