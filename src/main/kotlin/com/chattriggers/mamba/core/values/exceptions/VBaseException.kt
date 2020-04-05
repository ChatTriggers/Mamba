package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.core.values.VObjectType
import com.chattriggers.mamba.core.values.VType
import com.chattriggers.mamba.core.values.collections.VTuple

open class VBaseException(val args: VTuple) : VObject() {
    override val className = "BaseException"

    constructor() : this(VTuple())

    override fun toString() = StringBuilder().apply {
        append(className)
        append(": ")

        val items = args.items

        if (items.isNotEmpty()) {
            if (items.size == 1) {
                append(items[0])
            } else {
                append(items)
            }
        }
    }.toString()
}

object VBaseExceptionType : VType(LazyValue("VObjectType") { VObjectType })
