package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VExceptionWrapper
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
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
}

object VTupleType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            runtime.construct(VTupleIteratorType, listOf(assertSelfAs<VTuple>()))
        }
        addMethod("__call__") {
            runtime.construct(VTupleType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VTupleType) {
                TODO()
            }

            val iterable = when (argSize) {
                1 -> return@addMethod VTuple(emptyList())
                2 -> when (val arg = argumentRaw(1)) {
                    is Wrapper -> {
                        val value = arg.value

                        if (value is List<*>) {
                            if (value.isEmpty()) {
                                return@addMethod VTuple(emptyList())
                            } else if (value.isNotEmpty() && value.all { it is VObject }) {
                                @Suppress("UNCHECKED_CAST")
                                return@addMethod VTuple(value as List<VObject>)
                            }
                        }

                        arg.unwrap()
                    }
                    else -> arg.unwrap()
                }
                else -> TODO()
            }

            if (!runtime.isIterable(iterable)) {
                TODO("Error")
            }

            val list = mutableListOf<VObject>()

            while (true) {
                val next = runtime.getIteratorNext(iterable)
                if (next !is VExceptionWrapper) {
                    list.add(next)
                } else {
                    if (next is VStopIteration) break
                    else return@addMethod next
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
