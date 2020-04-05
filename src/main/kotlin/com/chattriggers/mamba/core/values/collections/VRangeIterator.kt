package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
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
            // TODO: StopIteration exception

            val (start, stop, step) = self.vrange
            val current = self.vrange.current

            when {
                step < 0 && current < stop -> return@addMethodDescriptor VNotImplemented
                step > 0 && current > stop -> return@addMethodDescriptor VNotImplemented
                start < stop && step < 0 -> return@addMethodDescriptor VNotImplemented
                start > stop && step > 0 -> return@addMethodDescriptor VNotImplemented
            }

            self.vrange.current += step
            current.toValue()
        }
    }
}