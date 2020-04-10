package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.Value

data class Slot(
    val key: Value,
    private var valueBacker: Value,
    val isStatic: Boolean,
    val isWritable: Boolean,
    val id: String? = null
) {
    var value: Value
        get() = valueBacker
        set(value) {
            if (!isWritable) {
                throw IllegalStateException()
            }

            valueBacker = value
        }
}

