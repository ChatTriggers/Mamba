package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.values.VObject

/**
 * Represents a method that can be bound to an
 * object. Makes no distinction between native
 * and user-defined methods
 */
interface IMethod : ICallable {
    val self: VObject?

    val isStatic: Boolean

    fun bind(newSelf: VObject)
}