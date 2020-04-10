package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.singletons.VNone

class VDictIterator(val vdict: VDict) : VObject(LazyValue { VDictIteratorType }) {
    internal var vdictKeys = vdict.dict.keys.toList()
    internal var cursor = 0

    override val className: String
        get() = "dict_iterator"

    override fun toString() = "<dict_iterator object>"
}

object VDictIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            assertSelfAs<VDictIterator>()
        }
        addMethod("__next__") {
            val self = assertSelfAs<VDictIterator>()

            when {
                self.vdict.dict.size != self.vdictKeys.size ->
                    TODO("RuntimeError: dictionary changed size during iteration")
                self.cursor >= self.vdictKeys.size -> VExceptionWrapper(VStopIteration.construct())
                else -> self.vdict.dict[self.vdictKeys[self.cursor++]]!!
            }.unwrap()
        }
        addMethod("__call__") {
            runtime.construct(VDictIteratorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VDictIteratorType) {
                TODO()
            }

            VDictIterator(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}
