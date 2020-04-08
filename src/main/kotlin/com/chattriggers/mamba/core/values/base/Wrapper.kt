package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.values.Value

class Wrapper(val value: Any) : Value {
    init {
        if (value is Value) {
            throw IllegalArgumentException("Wrapper expects a non-Value object")
        }
    }
}