package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VRangeIterator : VObject(LazyValue { VRangeIteratorType }) {
    override val className: String
        get() = "range_iterator"

    lateinit var vrange: VRange

    override fun toString() = "<range_iterator object>"
}

object VRangeIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            assertSelfAs<VRangeIterator>()
        }
        addMethod("__next__") {
            val self = assertSelfAs<VRangeIterator>()

            val (start, stop, step) = self.vrange
            val current = self.vrange.current.int

            val shouldThrow = step < 0 && current <= stop ||
                    step > 0 && current >= stop ||
                    start <= stop && step < 0 ||
                    start >= stop && step > 0

            if (shouldThrow) {
                return@addMethod VStopIteration.construct()
            }

            self.vrange.current.int += step
            current.toValue()
        }
        addMethod("__call__") {
            runtime.construct(VRangeIteratorType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VRangeIteratorType>(0)
            VRangeIterator()
        }
        addMethod("__init__") {
            assertSelfAs<VRangeIterator>().vrange = assertArgAs(1)
            VNone
        }
    }
}