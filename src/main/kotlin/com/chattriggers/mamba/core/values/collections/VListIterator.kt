package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.singletons.VNotImplemented

class VListIterator(internal val vlist: VList) : VObject(LazyValue("VListIteratorType") { VListIteratorType }) {
    internal var cursor = 0

    override val className = "list_iterator"

    override fun toString() = "<list_iterator object>"
}

object VListIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            assertSelfAs<VListIterator>()
        }
        addMethodDescriptor("__next__") {
            val self = assertSelfAs<VListIterator>()

            if (self.cursor >= self.vlist.list.size) {
                // TODO: StopIteration exception
                VNotImplemented
            } else {
                self.vlist.list[self.cursor++]
            }
        }
    }
}
