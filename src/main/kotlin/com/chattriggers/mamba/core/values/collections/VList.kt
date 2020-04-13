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

class VList : VObject(LazyValue("VListType") { VListType }) {
    override val className = "list"

    val list = mutableListOf<VObject>()

    override fun toString() = "[${list.joinToString()}]"
}

object VListType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VListType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VListType>(0)
            VList()
        }
        addMethod("__init__") {
            val self = assertSelfAs<VList>()

            if (argSize == 1) {
                return@addMethod VNone
            } else if (argSize > 2) {
                TODO()
            }

            val iterable = when (val arg = argumentValueRaw(1)) {
                is Wrapper -> {
                    val value = arg.value

                    if (value is List<*>) {
                        if (value.isNotEmpty()) {
                            @Suppress("UNCHECKED_CAST")
                            self.list.addAll(value as List<VObject>)
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
                    when (val result = runtime.getIteratorNext(iterator)) {
                        is VStopIteration -> break@loop
                        is VBaseException -> return@addMethod result
                        else -> self.list.add(result)
                    }
                }
            }

            VNone
        }

        addMethod("__iter__") {
            runtime.construct(VListIteratorType, listOf(assertSelfAs<VList>()))
        }
        addMethod("__getitem__") {
            val self = assertSelfAs<VList>()
            val index = assertArgAs<VInt>(1)
            self.list[index.int]
        }
        addMethod("__setitem__") {
            val self = assertSelfAs<VList>()
            val index = assertArgAs<VInt>(1)
            val obj = assertArgAs<VObject>(2)
            self.list[index.int] = obj

            VNone
        }
    }
}

fun <T : VObject> MutableList<T>.toValue(): VList {
    return ThreadContext.currentContext.runtime.construct(VListType, listOf(this)) as VList
}
