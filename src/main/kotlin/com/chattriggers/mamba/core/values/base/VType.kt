package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.*
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper

/**
 * Represents the type of an object.
 *
 * All objects have an associated type. This class represents
 * that type. Note that it is also an object, and it's type
 * is the Type type. This circularity is possible due to the
 * lazy nature of base classes: they are not evaluated until
 * something is called directly on them, avoiding the
 * circularity.
 */
open class VType : VObject {
    override val className = "type"

    constructor(parentType: LazyValue<VType>) : super(parentType)

    constructor() : super()

    protected fun addField(key: String, isStatic: Boolean = false, isWritable: Boolean = false, field: NCFType) {
        val wrapper = Wrapper(key)

        slotMap[wrapper] = Slot(
            wrapper,
            FieldWrapper(field),
            isStatic,
            isWritable
        )
    }

    protected fun addMethod(key: String, isStatic: Boolean = false, isWritable: Boolean = false, id: String? = null, method: NCMType) {
        val wrapper = Wrapper(key)

        slotMap[wrapper] = Slot(
            wrapper,
            MethodWrapper(key, method),
            isStatic,
            isWritable,
            id
        )
    }
}

object VTypeType : VType(LazyValue("VTypeType") { VTypeType })
