package com.chattriggers.mamba.core.values

abstract class VType : VObject() {
    override fun toString(): String {
        return "<class '$className'>"
    }
}
