package com.chattriggers.mamba.core.values.numbers

import com.chattriggers.mamba.core.values.VObject

sealed class VBool(val bool: Boolean) : VInt(if (bool) 1 else 0) {
    override val className: String
        get() = "bool"
}

object VTrue : VBool(true) {
    override fun toString() = "True"
}

object VFalse : VBool(false) {
    override fun toString() = "False"
}
