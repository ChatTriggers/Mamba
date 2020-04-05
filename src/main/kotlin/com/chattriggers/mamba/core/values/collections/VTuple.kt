package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

class VTuple(val items: List<VObject>) : VObject(LazyValue("VTupleType") { VTupleType }) {
    override val className = "tuple"

    override fun toString() = when (items.size) {
        1 -> "(${items[0]},)"
        else -> "(${items.joinToString()})"
    }

    companion object {
        val EMPTY_TUPLE = VTuple(emptyList())
    }
}

object VTupleType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            VTupleIterator(assertSelfAs())
        }
    }
}

fun <T : VObject> List<T>.toValue() = VTuple(this)
