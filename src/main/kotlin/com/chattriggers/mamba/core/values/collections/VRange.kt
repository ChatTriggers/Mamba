package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.VValueError
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.singletons.VNone

class VRange(val start: Int, val stop: Int, val step: Int = 1) : VObject(LazyValue("VRangeType") { VRangeType }) {
    internal var current = start

    override val className = "dict"

    constructor(stop: Int) : this(0, stop)

    override fun toString() = StringBuilder().apply {
        append("range(")

        append(start)
        append(", ")
        append(stop)

        if (step != -1) {
            append(", ")
            append(step)
        }

        append(")")
    }.toString()

    operator fun component1() = start
    operator fun component2() = stop
    operator fun component3() = step
}

object VRangeType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            runtime.construct(VRangeIteratorType, listOf(assertSelfAs<VRange>()))
        }
        addMethod("__call__") {
            runtime.construct(VRangeType, arguments())
        }
        addMethod("__new__") {
            val type = assertArgAs<VType>(0)

            if (type !is VRangeType) {
                TODO()
            }

            val first = assertArgAs<VInt>(1)
            val second = argAs<VInt>(2)
            val third = argAs<VInt>(3)

            if (third != null && third.int == 0) {
                return@addMethod VExceptionWrapper(VValueError.construct("range() arg 3 must not be 0"))
            }

            when {
                second == null -> VRange(first.int)
                third == null -> VRange(first.int, second.int)
                else -> VRange(first.int, second.int, third.int)
            }
        }
        addMethod("__init__") {
            VNone
        }
    }
}
