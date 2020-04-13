package com.chattriggers.mamba.core.values.collections

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.exceptions.VValueError
import com.chattriggers.mamba.core.values.numbers.VInt
import com.chattriggers.mamba.core.values.numbers.toValue
import com.chattriggers.mamba.core.values.singletons.VNone

class VRange : VObject(LazyValue("VRangeType") { VRangeType }) {
    lateinit var start: VInt
    lateinit var stop: VInt
    lateinit var step: VInt

    lateinit var current: VInt

    override val className = "dict"

    override fun toString() = StringBuilder().apply {
        append("range(")

        append(start)
        append(", ")
        append(stop)

        if (step.int != -1) {
            append(", ")
            append(step)
        }

        append(")")
    }.toString()

    operator fun component1() = start.int
    operator fun component2() = stop.int
    operator fun component3() = step.int
}

object VRangeType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__iter__") {
            runtime.construct(VRangeIteratorType, listOf(assertSelfAs<VRange>()))
        }
        addMethod("__call__") {
            runtime.construct(VRangeType, arguments())
        }
        addMethod("__new__", isStatic = true) {
            assertArgAs<VRangeType>(0)
            VRange()
        }
        addMethod("__init__") {
            val self = assertSelfAs<VRange>()

            val first = assertArgAs<VInt>(1)
            val second = argAs<VInt>(2)
            val third = argAs<VInt>(3)

            if (third != null && third.int == 0) {
                return@addMethod VValueError.construct("range() arg 3 must not be 0")
            }

            val start = when (second) {
                null -> 0.toValue() as VInt
                else -> first
            }

            self.start = start
            self.stop = second ?: first
            self.step = third ?: 1.toValue() as VInt
            self.current = start

            VNone
        }
    }
}
