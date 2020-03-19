package com.chattriggers.mamba.core.values

object VNone : VObject() {
    override val descriptor: ClassDescriptor
        get() = NoneDescriptor
}

object NoneDescriptor : ClassDescriptor(ObjectDescriptor)
