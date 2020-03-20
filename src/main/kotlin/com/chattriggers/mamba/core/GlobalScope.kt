package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*

object GlobalScope : VObject(GlobalScopeDescriptor)

object GlobalScopeDescriptor : ClassDescriptor() {
    init {
        addClassProperty("True", VTrue)
        addClassProperty("False", VFalse)
        addClassProperty("None", VNone)
        addClassProperty("...", VEllipsis)

        addClassMethod("abs") {
            assertArg<VObject>(0).callProperty(interp, "__abs__")
        }

        addClassMethod("dir") {
            interp.runtime.dir(arguments())
        }

        addClassMethod("str") {
            assertArg<VObject>(0).callProperty(interp, "__str__")
        }

        addClassMethod("print") {
            println(arguments().joinToString(separator = " "))
            VNone
        }
    }
}
