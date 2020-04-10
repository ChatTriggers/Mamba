package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.statements.FunctionNode
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VBuiltinMethodType
import com.chattriggers.mamba.core.values.base.VFunctionType
import com.chattriggers.mamba.core.values.unwrap

class ClassMethodBuilder(val ctx: ThreadContext, private val _args: List<Value>) {
    val runtime = ctx.runtime

    val argSize = _args.size

    fun argument(index: Int) = _args[index] as VObject

    fun argumentRaw(index: Int) = _args[index]

    fun arguments() = _args.map(Value::unwrap)

    inline fun <reified T : VObject> argAs(index: Int): T? {
        if (index >= argSize) return null
        return assertArgAs<T>(index)
    }

    inline fun <reified T : Value> assertArgAs(index: Int): T {
        val arg = argumentRaw(index)
        if (arg !is T)
            TODO()
        return arg
    }

    inline fun <reified T : Value> assertSelfAs(): T {
        return assertArgAs(0)
    }

    fun construct(type: VType, vararg args: Any): VObject {
        return runtime.construct(type, args.map {
            if (it !is VObject) Wrapper(
                it
            ) else it
        })
    }
}

class ClassFieldBuilder(val ctx: ThreadContext) {
    val runtime = ctx.runtime
}

// NativeClassMethodType
typealias NCMType = ClassMethodBuilder.() -> VObject

// NativeClassFieldType
typealias NCFType = ClassFieldBuilder.() -> VObject

data class MethodWrapper(
    val name: String,
    private val funcNode: FunctionNode? = null,
    private val method: NCMType? = null,
    override val self: VObject? = null
) : IMethod, Value {
    constructor(name: String, method: NCMType) : this(name, null, method, null)

    constructor(name: String, funcNode: FunctionNode) : this(name, funcNode, null, null)

    override fun bind(newSelf: VObject): Value {
        return this.copy(self = newSelf)
    }

    override fun call(ctx: ThreadContext, args: List<Value>): VObject {
        val newArgs = when (val s = self) {
            null -> args
            else -> listOf(s) + args
        }

        return when {
            funcNode != null -> funcNode.call(ctx, newArgs.map { it as VObject })
            method != null -> method.invoke(ClassMethodBuilder(ctx, newArgs))
            else -> TODO("Impossible")
        }
    }
}

class FieldWrapper(val field: NCFType) : Value
