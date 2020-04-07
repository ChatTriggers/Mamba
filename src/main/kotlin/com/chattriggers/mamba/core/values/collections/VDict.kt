package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone

class VDict(val dict: MutableMap<String, Value>) : VObject(LazyValue("VDictType") { VDictType }) {
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
        addMethod("__iter__") {
            runtime.construct(VDictIteratorType, listOf(assertSelfAs<VDict>()))
        }
        addMethod("__call__") {
            runtime.construct(VDictType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VDictType) {
                notImplemented()
            }

            val iterable = if (argSize > 0) argument(0) else /* VList.EMPTY_LIST */ VTuple()

            if (!Runtime.isIterable(iterable)) {
                notImplemented("Error")
            }

            val map = mutableMapOf<String, Value>()

            try {
                while (true) {
                    val it = runtime.getIterableNext(iterable)

                    if (!Runtime.isIterable(it)) {
                        notImplemented("Error")
                    }

                    try {
                        val key = runtime.getIterableNext(it).toString()
                        val value = runtime.getIterableNext(it)

                        map[key] = value
                    } catch (e: MambaException) {
                        if (e.reason is VStopIteration) {
                            notImplemented("TypeError")
                        }
                    }

                    try {
                        // Sub-iterable can only have two elements
                        runtime.getIterableNext(it)
                        notImplemented("ValueError")
                    } catch (e: MambaException) {
                        if (e.reason !is VStopIteration) {
                            throw e
                        }
                    }
                }
            } catch (e: MambaException) {
                if (e.reason !is VStopIteration) {
                    throw e
                }
            }

            VDict(map)
        }
        addMethod("__init__") {
            VNone
        }
    }
}

fun Map<String, VObject>.toValue() = VDict(this.toMutableMap())
