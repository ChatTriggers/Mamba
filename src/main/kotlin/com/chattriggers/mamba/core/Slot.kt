package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VTypeError

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
                throw MambaException(VTypeError("can't set write-only property '$key'"))
            }

            valueBacker = value
        }
}

