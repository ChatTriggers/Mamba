package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.unwrap

class VTuple(val items: List<VObject>) : VObject(LazyValue("VTupleType") { VTupleType }) {
    override val className = "tuple"

    override fun toString() = when (items.size) {
        0 -> "()"
        1 -> "(${items[0]},)"
        else -> "(${items.joinToString()})"
    }

    companion object {
        private var emptyTupleBacker: VTuple? = null

        val EMPTY_TUPLE: VTuple
            get() {
                if (emptyTupleBacker == null) {
                    emptyTupleBacker = ThreadContext.currentContext.runtime.construct(VTupleType, emptyList()) as VTuple
                }

                return emptyTupleBacker!!
            }
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
                TODO()
            }

            val iterable = when (argSize) {
                1 -> VTuple.EMPTY_TUPLE
                2 -> when (val arg = argumentRaw(1)) {
                    is Wrapper -> {
                        val value = arg.value

                        if (value !is List<*> || value.size == 0 || value[0] !is VObject) {
                            TODO()
                        }

                        @Suppress("UNCHECKED_CAST")
                        return@addMethod VTuple(value as List<VObject>)
                    }
                    else -> arg.unwrap()
                }
                else -> TODO()
            }

            if (!runtime.isIterable(iterable)) {
                TODO("Error")
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
