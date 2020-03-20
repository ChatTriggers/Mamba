package com.chattriggers.mamba.core.values

/**
 * Superclass of every value accessible in the Python runtime.
 *
 * This class just serves primarily as a map. It has two
 * subclasses: VObject, and LazyValue. The sole reason for
 * it's existence is to allow a value delegator (LazyValue).
 *
 * Value should be considered sealed, and should not be
 * inherited from by any class other than the two listed above
 *
 * @see VObject
 * @see LazyValue
 */
abstract class Value(protected open val descriptor: ClassDescriptor) : MutableMap<String, Value> by mutableMapOf()
