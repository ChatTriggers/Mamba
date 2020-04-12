package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.numbers.VInt
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
        addMethod("__call__") {
            runtime.construct(VTupleType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VTupleType) {
                TODO()
            }

            if (argSize == 1) {
                return@addMethod VTuple(emptyList())
            } else if (argSize > 2) {
                TODO()
            }

            val list = mutableListOf<VObject>()

            val iterable = when (val arg = argumentValueRaw(1)) {
                is Wrapper -> {
                    val value = arg.value

                    if (value is List<*>) {
                        if (value.isNotEmpty()) {
                            @Suppress("UNCHECKED_CAST")
                            list.addAll(value as List<VObject>)
                        }

                        null
                    } else arg.unwrap()
                }
                else -> arg.unwrap()
            }

            if (iterable != null) {
                if (!runtime.isIterable(iterable)) {
                    TODO("Error")
                }

                val iterator = runtime.getIterator(iterable)

                loop@
                while (true) {
                    when (val next = runtime.getIteratorNext(iterator)) {
                        is VStopIteration -> break@loop
                        is VBaseException -> return@addMethod next
                        else -> list.add(next)
                    }
                }
            }

            VTuple(list)
        }
        addMethod("__init__") {
            VNone
        }

        addMethod("__iter__") {
            runtime.construct(VTupleIteratorType, listOf(assertSelfAs<VTuple>()))
        }
        addMethod("__getitem__") {
            val self = assertSelfAs<VTuple>()
            val index = assertArgAs<VInt>(1)
            self.items[index.int]
        }
    }
}

fun <T : VObject> List<T>.toValue() = ThreadContext.currentContext.runtime.construct(VTupleType, listOf(this)) as VTuple
