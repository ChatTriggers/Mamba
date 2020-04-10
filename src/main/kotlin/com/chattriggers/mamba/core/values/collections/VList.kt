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

class VList(val list: MutableList<VObject>) : VObject(LazyValue("VListType") { VListType }) {
    override val className = "list"

    override fun toString() = "[${list.joinToString()}]"
}

object VListType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            runtime.construct(VListIteratorType, listOf(assertSelfAs<VList>()))
        }
        addMethod("__call__") {
            runtime.construct(VListType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VListType) {
                TODO()
            }

            val iterable = when (argSize) {
                1 -> return@addMethod VList(mutableListOf())
                2 -> when (val arg = argumentRaw(1)) {
                    is Wrapper -> {
                        val value = arg.value

                        if (value is MutableList<*>) {
                            if (value.isEmpty()) {
                                return@addMethod VList(mutableListOf())
                            } else if (value.isNotEmpty() && value.all { it is VObject }) {
                                @Suppress("UNCHECKED_CAST")
                                return@addMethod VList(value as MutableList<VObject>)
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

            val iterator = runtime.getIterator(iterable)
            val list = mutableListOf<VObject>()

            while (true) {
                val result = runtime.getIteratorNext(iterator)

                if (result is VExceptionWrapper) {
                    if (result.exception is VStopIteration) break
                    else return@addMethod result
                }

                list.add(result)
            }

            VList(list)
        }
        addMethod("__init__") {
            VNone
        }
    }
}

fun <T : VObject> MutableList<T>.toValue(): VList {
    return ThreadContext.currentContext.runtime.construct(VListType, listOf(this)) as VList
}
