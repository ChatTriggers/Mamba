package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VValueError
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone

class VRange(val start: Int, val stop: Int, val step: Int = 1) : VObject(LazyValue("VRangeType") { VRangeType }) {
    internal var current = start

    override val className = "dict"

    init {
        // TODO: ValueError
        if (step == 0) {
            throw MambaException(VValueError(VTuple("range() arg 3 must not be 0".toValue())))
        }
    }

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
                notImplemented()
            }

            val first = assertArgAs<VInt>(1)
            val second = argAs<VInt>(2)
            val third = argAs<VInt>(3)

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
