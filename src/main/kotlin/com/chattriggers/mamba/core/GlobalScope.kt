package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*

object GlobalScope : VObject(GlobalScopeDescriptor)

object GlobalScopeDescriptor : ClassDescriptor() {
    init {
        addClassProperty("True", VTrue)
        addClassProperty("False", VFalse)
        addClassProperty("None", VNone)
        addClassProperty("...", VEllipsis)

        addClassMethod("abs") { interp, args ->
            if (args.isEmpty() || args.size > 1)
                TODO()

            args[0].callProperty(interp, "__abs__")
        }

        addClassMethod("dir") { interp, args ->
            interp.runtime.dir(args)
        }

        addClassMethod("print") { _, args ->
            print(args.joinToString(separator = " "))
            VNone
        }
    }
}
