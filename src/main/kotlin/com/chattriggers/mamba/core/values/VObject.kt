package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.functions.IMethod
import com.chattriggers.mamba.core.values.functions.VFunctionWrapper
import com.chattriggers.mamba.core.values.functions.VNativeFunctionWrapper
import com.chattriggers.mamba.core.values.singletons.VNotImplemented
import com.chattriggers.mamba.core.values.singletons.toValue

/**
 * This is the base class of all regular Python types.
 *
 * Every type inherits from this class, and will typically
 * provide it's own base type for custom inheritance,
 * though that is of course not necessary. Normally,
 * anything that wants to work with a Python value in
 * Mamba will require it to be a VObject.
 *
 * @see Value
 * @see LazyValue
 */
open class VObject(private vararg val _baseTypes: LazyValue<VType>) : Value {
    protected val map = mutableMapOf<String, Value>()

    private val evaluatedBaseTypesBacker = mutableListOf<VType>()
    private var baseTypesEvaluated = false

    internal open val className = "object"

    protected open val baseTypes: List<LazyValue<VType>>
        get() = _baseTypes.toList()

    protected open val evaluatedBaseTypes: List<VType>
        get() {
            if (!baseTypesEvaluated) {
                evaluatedBaseTypesBacker.addAll(
                    baseTypes.map { it.valueProducer() }
                )

                bindMethodsFrom(evaluatedBaseTypesBacker)

                baseTypesEvaluated = true
            }

            return evaluatedBaseTypesBacker.toList()
        }

    private fun bindMethodsFrom(baseTypes: List<VType>) {
        baseTypes.forEach { baseType ->
            val keys = baseType.ownKeys.toList()
            for (key in keys) {
                val value = baseType[key]

                if (value is IMethod && !value.isStatic && key !in map) {
                    when (value) {
                        is VNativeFunctionWrapper -> this[key] = value.copy()
                        is VFunctionWrapper -> this[key] = value.copy()
                        else -> TODO()
                    }

                    (this.map[key] as IMethod).bind(this)
                }
            }

            // Bind methods to this object from the base's base types
            bindMethodsFrom(baseType.evaluatedBaseTypes)
        }
    }

    val ownKeys: MutableSet<String>
        get() = map.keys

    open val keys: MutableList<String>
        get() {
            val k = map.keys.toMutableList()

            for (baseType in evaluatedBaseTypes)
                k.addAll(baseType.keys)

            return k
        }

    open fun ownContains(key: String) = key in map

    open operator fun contains(key: String) = containsKey(key)

    open fun ownGet(key: String) = map[key]

    open operator fun get(key: String): VObject {
        // Force an evaluation before we look at our methods
        evaluatedBaseTypes

        if (key in map)
            return map[key].unwrap()

        for (baseType in evaluatedBaseTypes)
            if (key in baseType)
                return baseType[key].unwrap()

        TODO("AttributeError: $key")
    }

    open operator fun set(key: String, value: VObject) {
        map[key] = value
    }

    open fun containsKey(key: String): Boolean {
        if (key in map)
            return true

        for (baseType in evaluatedBaseTypes)
            if (key in baseType)
                return true

        return false
    }

    open fun getOrNull(key: String): VObject? {
        return if (key in this)
            this[key].unwrap()
        else null
    }

    open fun getOrDefault(key: String, defaultValue: VObject): VObject {
        return if (key in this)
            this[key].unwrap()
        else defaultValue
    }

    fun callProperty(interp: Interpreter, name: String, args: List<VObject> = emptyList()): VObject {
        val prop = this[name]
        return interp.runtime.call(prop, args)
    }
}

object VObjectType : VType() {
    init {
        addMethodDescriptor("__eq__") {
            val self = assertSelfAs<VObject>()
            val other = assertArgAs<VObject>(1)
            (self == other).toValue()
        }
        addMethodDescriptor("__ne__") {
            val self = assertSelfAs<VObject>()
            val other = assertArgAs<VObject>(1)
            val eq = self.callProperty(interp, "__eq__", listOf(other))
            (!runtime.toBoolean(eq)).toValue()
        }
        addMethodDescriptor("__lt__") { VNotImplemented }
        addMethodDescriptor("__le__") { VNotImplemented }
        addMethodDescriptor("__gt__") { VNotImplemented }
        addMethodDescriptor("__ge__") { VNotImplemented }
        addMethodDescriptor("__dir__") {
            assertSelfAs<VObject>().keys.sorted().toList().map(::VString).toValue()
        }
        addMethodDescriptor("__str__") {
            assertArgAs<VObject>(0).toString().toValue()
        }
    }
}

fun Value?.unwrap(): VObject {
    return when (this) {
        is LazyValue<*> -> valueProducer()
        is VObject -> this
        else -> TODO()
    }
}
