package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.functions.NativeClassMethod
import com.chattriggers.mamba.core.values.functions.VNativeFunctionWrapper

/**
 * Represents the type of an object.
 *
 * All objects have an associated type. This class represents
 * that type. Note that it is also an object, and it's type
 * is the Type type. This circularity is possible due to the
 * lazy nature of base classes: they are not evaluated until
 * something is called directly on them, avoiding the
 * circularity.
 */
open class VType(private val parentType: LazyValue<VType>? = null) : VObject(LazyValue("VTypeType") { VTypeType }) {
    override val className = "type"

    override val baseTypes: List<LazyValue<VType>>
        get() = parentType?.let(::listOf) ?: emptyList()

    protected fun addFieldDescriptor(name: String, value: VObject) {
        this[name] = value
    }

    protected fun addMethodDescriptor(name: String, func: NativeClassMethod) {
        this[name] = VNativeFunctionWrapper(name, func)
    }

    protected fun addStaticMethodDescriptor(name: String, func: NativeClassMethod) {
        this[name] = VNativeFunctionWrapper(name, func, true)
    }
}

object VTypeType : VType(LazyValue("VTypeType") { VTypeType })
