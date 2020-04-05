package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.singletons.VNotImplemented

class VRangeIterator(val vrange: VRange) : VObject(LazyValue { VRangeIteratorType }) {
    override val className: String
        get() = "range_iterator"

    override fun toString() = "<range_iterator object>"
}

object VRangeIteratorType : VType(LazyValue { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            assertSelfAs<VRangeIterator>()
        }
        addMethodDescriptor("__next__") {
            val self = assertSelfAs<VRangeIterator>()

            val (start, stop, step) = self.vrange
            val current = self.vrange.current

            val shouldThrow = step < 0 && current < stop ||
                    step > 0 && current > stop ||
                    start < stop && step < 0 ||
                    start > stop && step > 0

            if (shouldThrow) {
                throw MambaException(VStopIteration())
            }

            self.vrange.current += step
            current.toValue()
        }
    }
}