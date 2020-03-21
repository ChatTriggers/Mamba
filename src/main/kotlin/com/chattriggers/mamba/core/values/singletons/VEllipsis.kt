package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType

object VEllipsis : VObject(LazyValue("VEllipsisType") { VEllipsisType }) {
    override fun toString() = "Ellipsis"
}

object VEllipsisType : VType(LazyValue("VObjectType") { VObjectType })
