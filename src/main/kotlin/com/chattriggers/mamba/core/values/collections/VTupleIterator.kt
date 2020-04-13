package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.singletons.VNone

class VTupleIterator : VObject(LazyValue("VTupleIteratorType") { VTupleIteratorType }) {
    lateinit var vtuple: VTuple
    var cursor = 0

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
                VStopIteration.construct()
            } else {
                self.vtuple.items[self.cursor++]
            }
        }
        addMethod("__call__") {
            runtime.construct(VTupleIteratorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VTupleIteratorType>(0)
            VTupleIterator()
        }
        addMethod("__init__") {
            assertSelfAs<VTupleIterator>().vtuple = assertArgAs(1)
            VNone
        }
    }
}