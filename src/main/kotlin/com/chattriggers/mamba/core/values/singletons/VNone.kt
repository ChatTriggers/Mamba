package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType

object VNone : VObject(LazyValue("VNoneType") { VNoneType }) {
    override fun toString() = "None"
}

object VNoneType : VType(LazyValue("VObjectType") { VObjectType })
