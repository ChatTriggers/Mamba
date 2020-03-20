package com.chattriggers.mamba.core

import com.chattriggers.mamba.core.values.*

object GlobalScope : VObject(GlobalScopeDescriptor)

object GlobalScopeDescriptor : ClassDescriptor() {
    init {
        addFieldDescriptor("True", VTrue)
        addFieldDescriptor("False", VFalse)
        addFieldDescriptor("None", VNone)
        addFieldDescriptor("...", VEllipsis)

        addStaticMethodDescriptor("abs") {
            assertArg<VObject>(0).callProperty(interp, "__abs__")
        }

        addStaticMethodDescriptor("dir") {
            runtime.dir(arguments())
        }

        addStaticMethodDescriptor("str") {
            assertArg<VObject>(0).callProperty(interp, "__str__")
        }

        addStaticMethodDescriptor("print") {
            println(arguments().joinToString(separator = " "))
            VNone
        }
    }
}
