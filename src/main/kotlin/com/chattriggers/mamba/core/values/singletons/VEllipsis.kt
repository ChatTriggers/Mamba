package com.chattriggers.mamba.core.values

object VEllipsis : VObject(EllipsisDescriptor) {
    override fun toString() = "Ellipsis"
}

object EllipsisDescriptor : ClassDescriptor(ObjectDescriptor)
