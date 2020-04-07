package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.exceptions.notImplemented

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
        is Wrapper -> notImplemented("Unexpected Wrapper.unwrap()")
        else -> notImplemented()
    }
}
