package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.numbers.VFalse
import com.chattriggers.mamba.core.values.numbers.VTrue

object GlobalScope : VObject() {
    override fun toString(): String {
        // TODO: Is it possible to get a reference to the global scope?
        // In JavaScript, you can get it quite easily with 'this' on
        // the script level, but there is no 'this' in Python
        throw IllegalStateException("Invalid attempt to stringify the global scope object")
    }

    init {
        addProperty("True", VTrue)
        addProperty("False", VFalse)
        addProperty("None", VNone)
        addProperty("...", VEllipsis)

        addNativeMethod("abs") { interp, args ->
            if (args.isEmpty() || args.size > 1)
                TODO()

            args[0].callFunction(interp, "__abs__")
        }

        addNativeMethod("dir") { interp, args ->
            interp.runtime.dir(args)
        }

        addNativeMethod("print") { _, args ->
            print(args.joinToString(separator = " "))
            VNone
        }
    }
}