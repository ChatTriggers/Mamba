package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

object VNone : VObject(LazyValue("VNoneType") { VNoneType }) {
    override fun toString() = "None"
}

object VNoneType : VType(LazyValue("VObjectType") { VObjectType })
