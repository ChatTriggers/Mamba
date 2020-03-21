package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.singletons.VNotImplemented

class VTupleIterator(internal val vtuple: VTuple) : VObject(LazyValue("VTupleIteratorType") { VTupleIteratorType }) {
    internal var cursor = 0

    override val className = "tuple_iterator"

    override fun toString() = "<tuple_iterator object>"
}

object VTupleIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            assertSelf<VTupleIterator>()
        }
        addMethodDescriptor("__next__") {
            val self = assertSelf<VTupleIterator>()

            if (self.cursor >= self.vtuple.items.size) {
                // TODO: StopIteration exception
                VNotImplemented
            } else {
                self.vtuple.items[self.cursor++]
            }
        }
    }
}