package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone

class VListIterator(internal val vlist: VList) : VObject(LazyValue("VListIteratorType") { VListIteratorType }) {
    internal var cursor = 0

    override val className = "list_iterator"

    override fun toString() = "<list_iterator object>"
}

object VListIteratorType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            assertSelfAs<VListIterator>()
        }
        addMethod("__next__") {
            val self = assertSelfAs<VListIterator>()

            if (self.cursor >= self.vlist.list.size) {
                throw MambaException(VStopIteration())
            } else {
                self.vlist.list[self.cursor++]
            }
        }
        addMethod("__call__") {
            runtime.construct(VListIteratorType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VListIteratorType) {
                notImplemented()
            }

            VListIterator(assertArgAs(1))
        }
        addMethod("__init__") {
            VNone
        }
    }
}
