package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.functions.ClassMethodBuilder
import com.chattriggers.mamba.core.values.functions.VNativeFunction
import com.chattriggers.mamba.utils.ParentDeferredMap

open class ClassDescriptor(parent: ClassDescriptor? = null) : ParentDeferredMap<String, VObject>(parent) {
    protected fun addClassProperty(name: String, value: VObject) {
        ownPut(name, value)
    }
    
    protected fun addClassMethod(name: String, func: ClassMethodBuilder.() -> VObject) {
        ownPut(name, VNativeFunction(name, func))
    }
}