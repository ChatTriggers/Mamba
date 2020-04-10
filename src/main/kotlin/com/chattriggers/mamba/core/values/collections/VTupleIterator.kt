package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.singletons.VNone

class VTupleIterator(internal val vtuple: VTuple) : VObject(LazyValue("VTupleIteratorType") { VTupleIteratorType }) {
    internal var cursor = 0

    override val className = "tuple_iterator"

    override fun toString() = "<tuple_iterator object>"
}

object VTupleIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            assertSelfAs<VTupleIterator>()
        }
        addMethod("__next__") {
            val self = assertSelfAs<VTupleIterator>()

            if (self.cursor >= self.vtuple.items.size) {
                VExceptionWrapper(VStopIteration.construct())
            } else {
                self.vtuple.items[self.cursor++]
            }
        }
        addMethod("__call__") {
            runtime.construct(VTupleIteratorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VTupleIteratorType) {
                TODO()
            }

            VTupleIterator(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}