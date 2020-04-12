package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VStringType
import com.chattriggers.mamba.core.values.numbers.VFloatType
import com.chattriggers.mamba.core.values.numbers.VIntType
import com.chattriggers.mamba.core.values.singletons.VBoolType

data class Wrapper(val value: Any) : Value {
    init {
        if (value is Value) {
            throw IllegalArgumentException("Wrapper expects a non-Value object")
        }
    }

    fun toValue(): VObject = when (value) {
        is String -> VStringType
        is Double -> VFloatType
        is Float -> VFloatType
        is Int -> VIntType
        is Boolean -> VBoolType
        else -> throw IllegalStateException("Unrecognized wrapper type: ${value.javaClass.simpleName}")
    }.let {
        ThreadContext.currentContext.runtime.construct(it, listOf(value))
    }

    override fun toString() = value.toString()
}