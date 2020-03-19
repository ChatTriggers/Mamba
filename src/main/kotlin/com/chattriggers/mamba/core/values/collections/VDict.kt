package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.ClassDescriptor
import com.chattriggers.mamba.core.values.ObjectDescriptor
import com.chattriggers.mamba.core.values.VObject

class VDict<K : VObject, V : VObject>(val dict: MutableMap<K, V>) : VObject() {
    override val descriptor: ClassDescriptor
        get() = DictDescriptor
}

object DictDescriptor : ClassDescriptor(ObjectDescriptor) {

}

fun <K : VObject, V : VObject> MutableMap<K, V>.toValue() = VDict(this)

// Dummy parameter to differ the JVM signatures
fun <K : VObject, V : VObject> Map<K, V>.toValue(i: Int = 0) = VDict(this.toMutableMap())
