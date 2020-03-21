package com.chattriggers.mamba.core.values

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
