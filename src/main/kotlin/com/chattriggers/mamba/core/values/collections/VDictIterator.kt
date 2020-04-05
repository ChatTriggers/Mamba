package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented

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
            assertSelfAs<VDictIterator>()
        }
        addMethodDescriptor("__next__") {
            val self = assertSelfAs<VDictIterator>()

            when {
                self.vdict.dict.size != self.vdictKeys.size ->
                    notImplemented("RuntimeError: dictionary changed size during iteration")
                self.cursor >= self.vdictKeys.size -> throw MambaException(VStopIteration())
                else -> self.vdict.dict[self.vdictKeys[self.cursor++]]!!
            }.unwrap()
        }
    }
}
