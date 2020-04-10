package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.Runtime
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
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

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
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VListType) {
                TODO()
            }

            val iterable = when (argSize) {
                1 -> return@addMethod VList(mutableListOf())
                2 -> when (val arg = argumentRaw(1)) {
                    is Wrapper -> {
                        val value = arg.value

                        if (value !is MutableList<*> || value.size == 0 || value[0] !is VObject) {
                            TODO()
                        }

                        @Suppress("UNCHECKED_CAST")
                        return@addMethod VList(value as MutableList<VObject>)
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

            try {
                while (true) {
                    list.add(runtime.getIterableNext(iterator))
                }
            } catch (e: MambaException) {
                if (e.reason !is VStopIteration) {
                    throw e
                }
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
