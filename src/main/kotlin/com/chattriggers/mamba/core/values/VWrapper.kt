package com.chattriggers.mamba.core.values

class VWrapper(val value: Any) : Value() {
    override fun toString(): String {
        throw IllegalStateException("Attempt to stringify ")
    }
}