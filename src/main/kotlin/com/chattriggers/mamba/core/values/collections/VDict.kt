package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

class VDict(val dict: MutableMap<String, VObject>) : VObject(LazyValue("VDictType") { VDictType }) {
    override val className = "dict"
}

object VDictType : VType(LazyValue("VObjectType") { VObjectType })

fun Map<String, VObject>.toValue() = VDict(this.toMutableMap())
