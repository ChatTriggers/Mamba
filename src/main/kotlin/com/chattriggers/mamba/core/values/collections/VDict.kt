package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.singletons.VNone

class VDict(val dict: MutableMap<String, VObject>) : VObject(LazyValue("VDictType") { VDictType }) {
    override val className = "dict"

    override fun toString() = StringBuilder().apply {
        append("{")

        dict.entries.forEachIndexed { index, (key, value) ->
            append(key)
            append(": ")
            append(value)

            if (index < dict.size - 1)
                append(", ")
        }

        append("}")
    }.toString()
}

object VDictType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VDictType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VDictType) {
                TODO()
            }

            val iterable = when (argSize) {
                1 -> return@addMethod VTuple(emptyList())
                2 -> when (val arg = argumentValueRaw(1)) {
                    is Wrapper -> {
                        val value = arg.value

                        if (value is MutableMap<*, *>) {
                            if (value.isEmpty()) {
                                return@addMethod VDict(mutableMapOf())
                            } else if (value.isNotEmpty()) {
                                @Suppress("UNCHECKED_CAST")
                                return@addMethod VDict(value as MutableMap<String, VObject>)
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

            val map = mutableMapOf<String, VObject>()

            while (true) {
                val it = runtime.getIteratorNext(iterable)

                if (it is VExceptionWrapper) {
                    if (it.exception is VStopIteration) break
                    else return@addMethod it
                }

                if (!runtime.isIterable(it)) {
                    TODO("Error")
                }

                val key = runtime.getIteratorNext(it)
                val value = runtime.getIteratorNext(it)

                if (key is VExceptionWrapper)
                    return@addMethod key

                if (value is VExceptionWrapper)
                    return@addMethod key

                map[key.toString()] = value

                // Sub-iterable can only have two elements
                val next = runtime.getIteratorNext(it)
                if (next !is VExceptionWrapper) {
                    TODO("ValueError")
                }
            }

            VDict(map)
        }
        addMethod("__init__") {
            VNone
        }

        addMethod("__iter__") {
            runtime.construct(VDictIteratorType, listOf(assertSelfAs<VDict>()))
        }
        addMethod("__getitem__") {
            val self = assertSelfAs<VDict>()
            val index = assertArgAs<VString>(1)
            self.dict[index.string]!!
        }
        addMethod("__setitem__") {
            val self = assertSelfAs<VDict>()
            val index = assertArgAs<VString>(1)
            val obj = assertArgAs<VObject>(2)
            self.dict[index.string] = obj

            VNone
        }
    }
}

fun Map<String, VObject>.toValue() =
    ThreadContext.currentContext.runtime.construct(VDictType, listOf(this.toMutableMap()))
