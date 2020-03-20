package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.functions.NativeClassMethod
import com.chattriggers.mamba.utils.ParentDeferredMap

open class ClassDescriptor(parent: ClassDescriptor? = null) : ParentDeferredMap<String, PropertyDescriptor>(parent) {
    protected fun addFieldDescriptor(name: String, value: VObject) {
        ownPut(name, FieldDescriptor(value))
    }
    
    protected fun addMethodDescriptor(name: String, func: NativeClassMethod) {
        ownPut(name, MethodDescriptor(name, func))
    }

    protected fun addStaticMethodDescriptor(name: String, func: NativeClassMethod) {
        ownPut(name, StaticMethodDescriptor(name, func))
    }
}

sealed class PropertyDescriptor

data class FieldDescriptor(val value: VObject) : PropertyDescriptor()

data class MethodDescriptor(val name: String, val func: NativeClassMethod) : PropertyDescriptor()

data class StaticMethodDescriptor(val name: String, val func: NativeClassMethod) : PropertyDescriptor()
