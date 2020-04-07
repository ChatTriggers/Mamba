package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType

object VNotImplemented : VObject(LazyValue("VNotImplementedType") { VNotImplementedType }) {
    override fun toString() = "NotImplemented"
}

object VNotImplementedType : VType(LazyValue("VObjectType") { VObjectType })
