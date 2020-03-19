package com.chattriggers.mamba.core.values

object VNotImplemented : VObject() {
    override val descriptor: ClassDescriptor
        get() = NotImplementedDescriptor
}

object NotImplementedDescriptor : ClassDescriptor(ObjectDescriptor)
