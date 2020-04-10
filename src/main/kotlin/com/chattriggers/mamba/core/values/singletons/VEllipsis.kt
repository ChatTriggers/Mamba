package com.chattriggers.mamba.core.values.singletons

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType

object VEllipsis : VObject(LazyValue("VEllipsisType") { VEllipsisType }) {
    override val className = "ellipsis"

    override fun toString() = "Ellipsis"
}

object VEllipsisType : VType(LazyValue("VObjectType") { VObjectType })
