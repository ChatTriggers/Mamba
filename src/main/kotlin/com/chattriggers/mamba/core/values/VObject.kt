package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.ICallable
import com.chattriggers.mamba.core.values.functions.VFunction
import com.chattriggers.mamba.core.values.functions.VNativeBoundFunction
import com.chattriggers.mamba.core.values.functions.VNativeFunction

/**
 * This is the base class of all regular Python types.
 *
 * Every type inherits from this class, and will typically
 * provide it's own descriptor for custom inheritance,
 * though that is of course not necessary. Normally,
 * anything that wants to work with a Python value in
 * Mamba will require it to be a VObject.
 *
 * @see Value
 * @see LazyValue
 */
open class VObject(descriptor: ClassDescriptor = ObjectDescriptor) : Value(descriptor) {
    init {
        descriptor.keys.forEach(::bindMethod)
    }

    private fun bindMethod(name: String) {
        val prop = descriptor[name]!!

        this[name] = when (prop) {
            is FieldDescriptor -> prop.value
            is MethodDescriptor -> LazyValue {
                VNativeBoundFunction(name, prop.func, this)
            }
            is StaticMethodDescriptor -> LazyValue {
                VNativeFunction(name, prop.func)
            }
        }
    }

    fun getProperty(name: String): VObject? {
        if (name !in this)
            return null

        val prop = this[name]

        if (prop is LazyValue)
            return prop.valueProducer()

        return prop as VObject
    }

    fun callProperty(interp: Interpreter, name: String, args: List<VObject> = emptyList()): VObject {
        val prop = getProperty(name)

        if (prop == null || prop !is ICallable)
            TODO()

        // TODO: Bound method check
        return prop.call(interp, args)
    }
}

object ObjectDescriptor : ClassDescriptor() {
    init {
        addMethodDescriptor("__eq__") {
            val self = assertSelf<VObject>()
            val other = assertArg<VObject>(1)
            (self == other).toValue()
        }
        addMethodDescriptor("__ne__") {
            val self = assertSelf<VObject>()
            val other = assertArg<VObject>(1)
            val eq = self.callProperty(interp, "__eq__", listOf(other))
            (!runtime.toBoolean(eq)).toValue()
        }
        addMethodDescriptor("__lt__") { VNotImplemented }
        addMethodDescriptor("__le__") { VNotImplemented }
        addMethodDescriptor("__gt__") { VNotImplemented }
        addMethodDescriptor("__ge__") { VNotImplemented }

        addMethodDescriptor("__dir__") {
            assertSelf<VObject>().keys.sorted().toList().map(::VString).toValue()
        }
        addMethodDescriptor("__str__") {
            assertArg<VObject>(0).toString().toValue()
        }
    }
}
