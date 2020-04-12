package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.ClassFieldBuilder
import com.chattriggers.mamba.core.FieldWrapper
import com.chattriggers.mamba.core.MethodWrapper
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VBuiltinMethodType
import com.chattriggers.mamba.core.values.base.VFunctionType
import com.chattriggers.mamba.core.values.base.VObject

/**
 * Superclass of every value accessible in the Python runtime.
 *
 * The sole reason this class exists is to allow VObjects to
 * be lazily delegated by LazyValue. It's two children are
 * LazyValue and VObject. Any code outside of VObject should
 * use VObjects as a global base type. All instances of
 * Value in VObject that must be used are casted to VObject,
 * or retrieved from LazyValue, if applicable.
 *
 * Value should be considered sealed, and should not be
 * inherited from by any class other than the two listed above
 *
 * @see VObject
 * @see LazyValue
 */
interface Value

fun Value?.unwrap(): VObject {
    return when (this) {
        is LazyValue<*> -> valueProducer()
        is VObject -> this
        is MethodWrapper -> if (isNative) {
            ThreadContext.currentContext.runtime.construct(VBuiltinMethodType, listOf(this))
        } else {
            ThreadContext.currentContext.runtime.construct(VFunctionType, listOf(this))
        }
        is FieldWrapper -> this.field(ClassFieldBuilder(ThreadContext.currentContext))
        is Wrapper -> ThreadContext.currentContext.runtime.toVObject(this.value)
        else -> TODO()
    }
}
