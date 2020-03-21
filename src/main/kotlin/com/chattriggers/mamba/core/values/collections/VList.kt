package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.singletons.VNone

class VList(val list: MutableList<VObject>) : VObject(LazyValue("VListType") { VListType }) {
    override val className = "list"

    override fun toString() = "[${list.joinToString()}]"
}

object VListType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            VListIterator(assertSelf())
        }
    }
}

fun <T : VObject> MutableList<T>.toValue() = VList(this.toMutableList())
