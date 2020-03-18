package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VNativeFunction
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VObject

object GlobalScope : VObject() {
    override fun toString(): String {
        // TODO: Is it possible to get a reference to the global scope?
        // In JavaScript, you can get it quite easily with 'this' on
        // the script level, but there is no 'this' in Python
        throw IllegalStateException("Invalid attempt to stringify the global scope object")
    }

    init {
        slots["print"] = VNativeFunction("print") { _, args ->
            print(args.joinToString(separator = " "))
            VNone
        }
    }
}