package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.singletons.VNotImplemented

class VDictIterator(val vdict: VDict) : VObject(LazyValue { VDictIteratorType }) {
    internal var vdictKeys = vdict.dict.keys.toList()
    internal var cursor = 0

    override val className: String
        get() = "dict_iterator"

    override fun toString() = "<dict_iterator object>"
}

object VDictIteratorType : VType(LazyValue { VObjectType }) {
    init {
        addMethodDescriptor("__iter__") {
            assertSelf<VDictIterator>()
        }
        addMethodDescriptor("__next__") {
            val self = assertSelf<VDictIterator>()
            // TODO: StopIteration exception

            when {
                self.vdict.dict.size != self.vdictKeys.size ->
                    TODO("RuntimeError: dictionary changed size during iteration")
                self.cursor >= self.vdictKeys.size -> VNotImplemented
                else -> self.vdict.dict[self.vdictKeys[self.cursor++]]!!
            }.unwrap()
        }
    }
}
