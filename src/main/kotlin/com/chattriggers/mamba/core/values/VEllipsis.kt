package com.chattriggers.mamba.core.values

object VEllipsis : VObject() {
    override val descriptor: ClassDescriptor
        get() = EllipsisDescriptor
}

object EllipsisDescriptor : ClassDescriptor(ObjectDescriptor)
