package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VType

object GlobalScope : VObject() {
    val TYPE = object : VType() {
        override val className: String
            get() = throw IllegalStateException("Invalid attempt to get class name of global object")

        init {
            addNativeMethod("print") { _, args ->
                print(args.joinToString(separator = " "))
                VNone
            }
        }
    }

    override fun toString(): String {
        // TODO: Is it possible to get a reference to the global scope?
        // In JavaScript, you can get it quite easily with 'this' on
        // the script level, but there is no 'this' in Python
        throw IllegalStateException("Invalid attempt to stringify the global scope object")
    }

    init {
        inherit(TYPE)
    }
}