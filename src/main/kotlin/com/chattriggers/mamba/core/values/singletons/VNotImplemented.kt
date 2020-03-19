package com.chattriggers.mamba.core.values

object VNotImplemented : VObject(NotImplementedDescriptor) {
    override fun toString() = "NotImplemented"
}

object NotImplementedDescriptor : ClassDescriptor(ObjectDescriptor)
