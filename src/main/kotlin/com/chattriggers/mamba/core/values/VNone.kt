package com.chattriggers.mamba.core.values

object VNone : VObject(NoneDescriptor) {
    override fun toString() = "None"
}

object NoneDescriptor : ClassDescriptor(ObjectDescriptor)
