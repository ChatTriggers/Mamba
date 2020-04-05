package com.chattriggers.mamba.core.values.functions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.notImplemented

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
    override val className = "builtin_function_or_method" // from CPython

    override var self: VObject? = null

    override fun bind(newSelf: VObject) {
        if (self != null)
            notImplemented()
        self = newSelf
    }

    override fun call(interp: Interpreter, args: List<VObject>): VObject {
        return when {
            name == "__call__" && self is IMethod -> (self as IMethod).call(interp, args)
            self == null -> method(ClassMethodBuilder(interp, args))
            else -> method(ClassMethodBuilder(interp, listOf(self!!) + args))
        }
    }

    override fun toString() = if (self == null)
        "<built-in function $name>"
    else
        "<method '$name' of '${self!!.className}' objects>"
}
