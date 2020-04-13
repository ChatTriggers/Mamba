package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.unwrap

class VString : VObject(LazyValue("VStringType") { VStringType }) {
    override val className = "str"

    var string = ""

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
            assertArgAs<VStringType>(0)
            VString()
        }
        addMethod("__init__") {
            val self = assertSelfAs<VString>()

            if (argSize == 1) return@addMethod VNone

            self.string= when (val arg = argumentValueRaw(1)) {
                is Wrapper -> arg.value.toString()
                else -> runtime.callProp(arg.unwrap(), "__str__").toString()
            }

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
