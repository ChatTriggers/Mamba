package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.ICallable

/**
 * Represents "normal" Python objects. Classes that
 * inherit from this class are able to be created by
 * user scripts.
 */
open class VObject : MutableMap<String, VObject> by mutableMapOf() {
    protected open val descriptor: ClassDescriptor
        get() = ObjectDescriptor

    internal open fun lookup(name: String): VObject? {
        if (name in this)
            return this[name]
        return descriptor.lookup(name)
    }

    internal open fun has(name: String): Boolean {
        return name in this || descriptor.has(name)
    }

    internal fun set(name: String, value: VObject): VObject? {
        if (name in this) {
            val ret = this[name]
            this[name] = value
            return ret
        }

        // TODO: Probably should keep the descriptors clean and
        // set the name in this object
        return descriptor.set(name, value)
    }

    fun callProperty(interp: Interpreter, name: String, args: List<VObject> = emptyList()): VObject {
        val prop = lookup(name) ?: TODO()

        if (prop !is ICallable)
            TODO()

        // TODO: Bound method check
        return prop.call(interp, listOf(this) + args)
    }
}

object ObjectDescriptor : ClassDescriptor() {
    init {
        addClassMethod("__dir__") { _, args ->
            val self = assertSelf<VObject>(args)
            self.keys.toList().map(::VString).toValue()
        }
    }
}
