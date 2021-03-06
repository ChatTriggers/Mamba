package com.chattriggers.mamba.core.values.exceptions

import com.chattriggers.mamba.core.CallFrame
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VObjectType
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.VTuple
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.singletons.VNone

open class VBaseException(
    type: LazyValue<VType> = LazyValue("VBaseExceptionType") { VBaseExceptionType }
) : VObject(type) {
    override val className = "BaseException"

    lateinit var args: VTuple

    private val savedCallStack = ThreadContext.currentContext.interp.callStack.toList().map { it.copy() }
    var callStack: List<CallFrame>? = null

    fun initializeCallstack(lineNumber: Int) {
        if (callStack != null) return

        callStack = savedCallStack

        for ((index, callFrame) in callStack!!.withIndex()) {
            callFrame.lineNumber = if (index == callStack!!.lastIndex) {
                lineNumber
            } else {
                callStack!![index + 1].lineNumber
            }
        }
    }

    fun printTrace(ctx: ThreadContext) {
        println("Traceback (most recent call last):")

        callStack!!.forEach { cs ->
            println("  File \"${cs.source}\", line ${cs.lineNumber}, in ${cs.name}")
            println("    ${ctx.interp.lines[cs.lineNumber - 1].trim()}")
        }

        println(toString())
    }

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
            VBaseException()
        }
        addMethod("__init__") {
            assertSelfAs<VBaseException>().args = arguments().drop(1).let { args ->
                runtime.construct(VTupleType, listOf(args)) as VTuple
            }
            VNone
        }
    }
}
