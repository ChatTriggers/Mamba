package com.chattriggers.mamba.core.values

open class VType(val targetDescriptor: ClassDescriptor) : VObject() {
    override val descriptor: ClassDescriptor
        get() = TypeDescriptor
}

object TypeDescriptor : ClassDescriptor(ObjectDescriptor) {

}
