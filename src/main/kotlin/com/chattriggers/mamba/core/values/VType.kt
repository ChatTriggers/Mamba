package com.chattriggers.mamba.core.values

open class VType(val targetDescriptor: ClassDescriptor) : VObject(TypeDescriptor)

object TypeDescriptor : ClassDescriptor(ObjectDescriptor)
