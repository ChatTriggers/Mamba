package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.ClassDescriptor
import com.chattriggers.mamba.core.values.ObjectDescriptor
import com.chattriggers.mamba.core.values.VObject

class VList<T : VObject>(val list: MutableList<T>) : VObject(ListDescriptor)

object ListDescriptor : ClassDescriptor(ObjectDescriptor)

fun <T : VObject> MutableList<T>.toValue() = VList(this)

// Dummy parameter to differ the JVM signatures
fun <T : VObject> List<T>.toValue(i: Int = 0) = VList(this.toMutableList())
