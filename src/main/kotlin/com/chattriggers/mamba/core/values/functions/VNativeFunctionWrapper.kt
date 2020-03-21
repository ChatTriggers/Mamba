package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
/**
 * VObject type for native functions
 *
 * This type wraps a FunctionNode and provides
 * basic method functionalities, such as binding
 * and the ability to be static
 *
 * @see NativeClassMethod
 */
data class VNativeFunctionWrapper(
    val name: String,
    val method: NativeClassMethod,
    override val isStatic: Boolean = false
) : VObject(LazyValue("VMethodWrapperType") { VFunctionWrapperType }), IMethod {
    override var self: VObject? = null

    override fun bind(newSelf: VObject) {
        if (self != null)
            TODO()
        self = newSelf
    }

    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return when {
            name == "__call__" && self is IMethod -> (self as IMethod).call(interp, args)
            self == null -> method(ClassMethodBuilder(interp, args))
            else -> method(ClassMethodBuilder(interp, listOf(self!!) + args))
        }
    }

    override fun toString() = "<method '$name' of '<TODO: Type class names>' objects>"
}
