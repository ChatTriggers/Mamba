package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone

class VTuple(val items: List<VObject>) : VObject(LazyValue("VTupleType") { VTupleType }) {
    override val className = "tuple"

    constructor() : this(emptyList())

    constructor(vararg items: VObject) : this(items.toList())

    override fun toString() = when (items.size) {
        0 -> "()"
        1 -> "(${items[0]},)"
        else -> "(${items.joinToString()})"
    }

    companion object {
        val EMPTY_TUPLE = ThreadContext.currentContext.runtime.construct(VTupleType, emptyList())
    }
}

object VTupleType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            runtime.construct(VTupleIteratorType, listOf(assertSelfAs<VTuple>()))
        }
        addMethod("__call__") {
            runtime.construct(VTupleType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VTupleType) {
                notImplemented()
            }

            if (argSize == 0) {
                return@addMethod VTuple.EMPTY_TUPLE
            }

            val iterable = argument(0)

            if (!Runtime.isIterable(iterable)) {
                notImplemented("Error")
            }

            val list = mutableListOf<VObject>()

            try {
                while (true) {
                    list.add(runtime.getIterableNext(iterable))
                }
            } catch (e: MambaException) {
                if (e.reason !is VStopIteration) {
                    throw e
                }
            }

            VTuple(list)
        }
        addMethod("__init__") {
            VNone
        }
    }
}

fun <T : VObject> List<T>.toValue() = ThreadContext.currentContext.runtime.construct(VTupleType, listOf(this)) as VTuple
