package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.ir.nodes.statements.FunctionNode

/**
 * VObject type for user-defined functions.
 *
 * This type wraps a FunctionNode and provides
 * basic method functionalities, such as binding
 * and the ability to be static
 *
 * @see FunctionNode
 */
data class VFunctionWrapper(
    val name: String,
    val method: FunctionNode,
    override val isStatic: Boolean = false
) : VObject(LazyValue("VMethodWrapperType") { VFunctionWrapperType }), IMethod {
    override val className = "function"

    override var self: VObject? = null

    override fun bind(newSelf: VObject) {
        if (self != null)
            TODO()
        self = newSelf
    }

    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return when (self) {
            null -> method.call(interp, args)
            else -> method.call(interp, listOf(self!!) + args)
        }
    }

    override fun toString() = "<function $name>"
}

object VFunctionWrapperType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__call__") {
            argument(0).callProperty(interp, "__call__", arguments())
        }
    }
}