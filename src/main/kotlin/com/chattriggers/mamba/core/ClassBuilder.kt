package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.expressions.Argument
import com.chattriggers.mamba.ast.nodes.statements.FunctionNode
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VBuiltinMethodType
import com.chattriggers.mamba.core.values.base.VFunctionType
import com.chattriggers.mamba.core.values.unwrap

class ClassMethodBuilder(val ctx: ThreadContext, private val _args: List<Argument>) {
    val runtime = ctx.runtime

    val argSize = _args.size

    fun argumentValueRaw(index: Int) = _args[index].value

    fun argumentRaw(index: Int) = _args[index]

    fun argument(index: Int) = _args[index].value as VObject

    fun argumentValuesRaw() = _args.map { it.value }

    fun argumentsRaw() = _args

    fun arguments() = _args.map { it.value as VObject }

    fun namedArgument(name: String) = _args.firstOrNull { it.name == name }

    fun namedArgumentValue(name: String) = namedArgument(name)?.value

    // Equivalent to **kwargs in a python function definition
    fun namedArguments() = _args.filter { it.name != null }

    inline fun <reified T : Value> assertArgAs(index: Int): T {
        val arg = argumentValueRaw(index)
        if (arg !is T)
            TODO()
        return arg
    }

    inline fun <reified T : Value> assertSelfAs(): T {
        return assertArgAs(0)
    }

    fun construct(type: VType, vararg args: Any): VObject {
        return runtime.construct(type, args.toList())
    }

    fun constructFromArgs(type: VType, args: List<Argument>): VObject {
        return runtime.constructFromArgs(type, args)
    }

    inline fun <reified T : VObject> argAs(index: Int): T? {
        if (index >= argSize) return null
        return assertArgAs<T>(index)
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

    override fun call(ctx: ThreadContext, args: List<Argument>): VObject {
        val newArgs = when (val s = self) {
            null -> args
            else -> listOf(Argument(s, null, spread = false, kwSpread = false)) + args
        }

        return when {
            funcNode != null -> funcNode.call(ctx, newArgs)
            method != null -> method.invoke(ClassMethodBuilder(ctx, newArgs))
            else -> TODO("Impossible")
        }
    }
}

class FieldWrapper(val field: NCFType) : Value
