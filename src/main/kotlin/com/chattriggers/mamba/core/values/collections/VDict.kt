package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

class VDict<K : VObject, V : VObject>(val dict: MutableMap<K, V>) : VObject(LazyValue("VDictType") { VDictType }) {
    override val className = "dict"
}

object VDictType : VType(LazyValue("VObjectType") { VObjectType })

fun <K : VObject, V : VObject> MutableMap<K, V>.toValue() = VDict(this)

// Dummy parameter to differ the JVM signatures
fun <K : VObject, V : VObject> Map<K, V>.toValue(i: Int = 0) = VDict(this.toMutableMap())
