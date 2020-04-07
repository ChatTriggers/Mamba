package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.values.base.VObject

/**
 * This class allowed VObjects to be delegated and handled
 * separately.
 *
 * Python's type system is naturally circular. For example,
 * consider the built-in type {@code object}. It is an instance
 * of the {@code type} class. But {@code object} represents
 * the built-in object class -- when you call it, it calls the
 * {@code object} constructor. This produces the following
 * hierarchy:
 *
 * <blockquote><pre>
 *      object class -> type class -> built-in object -> object class
 * </pre></blockquote>
 *
 * It is then necessary to construct VObjects in the VObject
 * constructor, which is of course not possible. So, any time
 * VObject needs to construct a VObject in it's constructor,
 * it instead constructs a LazyValue, which will produce it's
 * value at a later time, outside of the constructor.
 *
 * This powers the ability for automatic infinite property chaining,
 * such as
 * {@code 'AAaa'.lower.__call__.__call__.__call__.# etc..}
 */
class LazyValue<T : VObject>(val name: String? = null, val valueProducer: () -> T) : Value