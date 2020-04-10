package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VString(val string: String) : VObject(LazyValue("VStringType") { VStringType }) {
    override val className = "str"

    override fun toString() = string

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?) = other is VString && string == other.string
}

object VStringType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__", id = "str_call") {
            runtime.construct(VStringType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VStringType) {
                val name = type.className
                return@addMethod VExceptionWrapper(VTypeError.construct("string.__new__($name) is not safe, use $name.__new__()"))
            }

            when (val arg = argumentValueRaw(1)) {
                is Wrapper -> VString(arg.value.toString())
                else -> VString(runtime.callProp(arg.unwrap(), "__str__").toString())
            }
        }
        addMethod("__init__") {
            VNone
        }

        addMethod("lower") {
            assertSelfAs<VString>().string.toLowerCase().toValue()
        }

        addMethod("__add__") {
            val self = assertSelfAs<VString>()
            val other = assertArgAs<VString>(1)

            (self.string + other.string).toValue()
        }
        addMethod("__int__") {
            val self = assertSelfAs<VString>()
            self.string.toInt().toValue()
        }
    }
}

fun String.toValue() = ThreadContext.currentContext.runtime.construct(VStringType, listOf(this))
