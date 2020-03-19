package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.utils.ParentDeferredMap

/**
 * Represents "normal" Python objects. Classes that
 * inherit from this class are able to be created by
 * user scripts.
 */
open class VObject(descriptor: ClassDescriptor = ObjectDescriptor) : ParentDeferredMap<String, VObject>(descriptor) {
    fun callProperty(interp: Interpreter, name: String, args: List<VObject> = emptyList()): VObject {
        val prop = this[name]

        if (prop == null || prop !is ICallable)
            TODO()

        // TODO: Bound method check
        return prop.call(interp, listOf(this) + args)
    }
}

object ObjectDescriptor : ClassDescriptor() {
    init {
        addClassMethod("__eq__") { _, args ->
            val self = assertSelf<VObject>(args)
            val other = assertArg<VObject>(args, 1)
            (self == other).toValue()
        }
        addClassMethod("__ne__") { interp, args ->
            val self = assertSelf<VObject>(args)
            val other = assertArg<VObject>(args, 1)
            val eq = self.callProperty(interp, "__eq__", listOf(other))
            (!interp.runtime.toBoolean(eq)).toValue()
        }
        addClassMethod("__lt__") { _, _ -> VNotImplemented }
        addClassMethod("__le__") { _, _ -> VNotImplemented }
        addClassMethod("__gt__") { _, _ -> VNotImplemented }
        addClassMethod("__ge__") { _, _ -> VNotImplemented }

        addClassMethod("__dir__") { _, args ->
            val self = assertSelf<VObject>(args)
            self.keys.toList().map(::VString).toValue()
        }
        addClassMethod("__str__") { _, args ->
            val arg = assertArg<VObject>(args, 0)
            arg.toString().toValue()
        }
    }
}
