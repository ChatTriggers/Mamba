package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.functions.VNativeFunction
import com.chattriggers.mamba.utils.ParentDeferredMap

open class ClassDescriptor(parent: ClassDescriptor? = null) : ParentDeferredMap<String, VObject>(parent) {
    protected fun addClassProperty(name: String, value: VObject) {
        ownPut(name, value)
    }
    
    protected fun addClassMethod(name: String, func: (interp: Interpreter, args: List<VObject>) -> VObject) {
        ownPut(name, VNativeFunction(name, func))
    }

    companion object {
        inline fun <reified T : VObject> assertSelf(args: List<VObject>): T {
            if (args.isEmpty() || args[0] !is T)
                TODO()
            return args[0] as T
        }

        inline fun <reified T : VObject> assertArg(args: List<VObject>, index: Int): T {
            if (index >= args.size || args[index] !is T)
                TODO()
            return args[index] as T
        }
    }
}