package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.VNativeFuncType
import com.chattriggers.mamba.core.values.functions.VNativeMethod

abstract class VType : VObject() {
    override fun toString(): String {
        return "<class '$className'>"
    }

    companion object {
        val TYPE = object : VType() {
            override val className: String
                get() = "type"
        }
    }
}
