package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

open class VBaseException(
    val args: VTuple,
    type: LazyValue<VType> = LazyValue("VBaseExceptionType") { VBaseExceptionType }
) : VObject(type) {
    override val className = "BaseException"

    override fun toString() = StringBuilder().apply {
        append(className)

        val items = args.items

        if (items.isNotEmpty()) {
            append(": ")

            if (items.size == 1) {
                append(items[0])
            } else {
                append(items)
            }
        }
    }.toString()
}

object VBaseExceptionType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            runtime.construct(VBaseExceptionType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VBaseExceptionType>(0)

            val argument = when (val arg = assertArgAs<VObject>(1)) {
                is VTuple -> arg
                else -> runtime.construct(VTupleType, listOf(listOf(arg))) as VTuple
            }

            VValueError(argument)
        }
        addMethod("__init__") {
            VNone
        }
    }
}
