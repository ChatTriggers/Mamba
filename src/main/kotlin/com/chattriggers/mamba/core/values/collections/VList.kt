package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

class VList<T : VObject>(val list: MutableList<T>) : VObject(LazyValue("VListType") { VListType }) {
    override fun toString() = "[${list.joinToString()}]"
}

object VListType : VType(LazyValue("VObjectType") { VObjectType })

fun <T : VObject> MutableList<T>.toValue() = VList(this)

// Dummy parameter to differ the JVM signatures
fun <T : VObject> List<T>.toValue(i: Int = 0) = VList(this.toMutableList())
