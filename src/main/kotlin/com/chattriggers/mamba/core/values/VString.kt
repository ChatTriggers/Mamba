package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.singletons.VNone

class VString(val string: String) : VObject(LazyValue("VStringType") { VStringType }) {
    override val className = "str"

    override fun toString() = string

    override fun hashCode() = string.hashCode()

    override fun equals(other: Any?) = other is VString && string == other.string
}

object VStringType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("lower") {
            assertSelfAs<VString>().string.toLowerCase().toValue()
        }
        addMethod("__add__") {
            val self = assertSelfAs<VString>()
            val other = assertArgAs<VString>(1)

            (self.string + other.string).toValue()
        }
        addMethod("__call__", id = "str_call") {
            runtime.construct(VStringType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VStringType) {
                val name = type.className
                throw MambaException(VTypeError("string.__new__($name) is not safe, use $name.__new__()"))
            }

            when (val arg = argumentRaw(1)) {
                is Wrapper -> VString(arg.value.toString())
                else -> VString(runtime.callProperty(arg.unwrap(), "__str__").toString())
            }
        }
        addMethod("__init__") {
            VNone
        }
    }
}

fun String.toValue() = ThreadContext.currentContext.runtime.construct(VStringType, listOf(this))
