package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.ast.nodes.statements.FunctionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.toValue

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

    protected fun addField(key: String, value: VObject, isStatic: Boolean = false, isWritable: Boolean = false) {
        addField(key.toValue(), value, isStatic, isWritable)
    }

    protected fun addField(key: VObject, value: VObject, isStatic: Boolean = false, isWritable: Boolean = false) {
        slotMap[key] = Slot(key, value, isStatic, isWritable)
    }

    protected fun addMethod(key: String, isStatic: Boolean = false, isWritable: Boolean = false, id: String? = null, method: NativeClassMethod) {
        addMethod(key.toValue(), isStatic, isWritable, id, method)
    }

    protected fun addMethod(key: String, method: FunctionNode, isStatic: Boolean = false, isWritable: Boolean = false, id: String? = null) {
        addMethod(key.toValue(), method, isStatic, isWritable, id)
    }

    protected fun addMethod(key: VObject, isStatic: Boolean = false, isWritable: Boolean = false, id: String? = null, method: NativeClassMethod) {
        slotMap[key] = Slot(
            key,
            ThreadContext.currentContext.runtime.construct(VNativeMethodType, listOf(method)),
            isStatic,
            isWritable,
            id
        )
    }

    protected fun addMethod(key: VObject, method: FunctionNode, isStatic: Boolean = false, isWritable: Boolean = false, id: String? = null) {
        slotMap[key] = Slot(
            key,
            ThreadContext.currentContext.runtime.construct(VMethodType, listOf(method)),
            isStatic,
            isWritable,
            id
        )
    }
}

object VTypeType : VType(LazyValue("VTypeType") { VTypeType })
