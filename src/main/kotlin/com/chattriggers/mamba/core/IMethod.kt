package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.expressions.Argument
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VObject

/**
 * Represents any object that can be called.
 * This includes both native functions and
 * user-defined functions
 */
interface IMethod {
    val self: VObject?

    fun call(ctx: ThreadContext, args: List<Argument>): VObject

    fun bind(newSelf: VObject): Value
}