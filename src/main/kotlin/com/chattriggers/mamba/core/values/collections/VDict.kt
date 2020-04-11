package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.VValueError
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
            constructFromArgs(VDictType, argumentsRaw())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VDictType) {
                TODO()
            }

            if (argSize == 1) {
                return@addMethod VTuple(emptyList())
            }

            val map = mutableMapOf<String, VObject>()
            val arg1 = argumentRaw(1)

            val iterable = when {
                arg1.name != null -> null
                arg1.value is Wrapper -> {
                    val wrapper = arg1.value
                    val value = wrapper.value

                    if (value is Map<*, *>) {
                        // Put values directly into the map
                        if (value.isNotEmpty()) {
                            @Suppress("UNCHECKED_CAST")
                            map.putAll(value as MutableMap<String, VObject>)
                        }
                        null
                    } else wrapper.unwrap()
                }
                else -> arg1.value.unwrap()
            }

            if (iterable != null) {
                if (!runtime.isIterable(iterable)) {
                    TODO("Error")
                }

                val iterator = runtime.getIterator(iterable).ifException { return@addMethod it }
                var i = 0

                while (true) {
                    val subIterable = runtime.getIteratorNext(iterator)

                    if (subIterable is VExceptionWrapper) {
                        if (subIterable.exception is VStopIteration) break
                        else return@addMethod subIterable
                    }

                    if (!runtime.isIterable(subIterable)) {
                        TODO("Error")
                    }

                    val subIterator = runtime.getIterator(subIterable).ifException { return@addMethod it }

                    val key = runtime.getIteratorNext(subIterator).ifException { return@addMethod it }
                    val value = runtime.getIteratorNext(subIterator).ifException { return@addMethod it }

                    map[key.toString()] = value

                    // Sub-iterator can only have two elements
                    runtime.getIteratorNext(subIterator).ifNotException {
                        return@addMethod VValueError.construct("dictionary update sequence $i has length >2; 2 is required")
                    }

                    i++
                }
            }

            val namedArgs = namedArguments()

            for (arg in namedArgs) {
                map[arg.name!!] = arg.value.unwrap()
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
