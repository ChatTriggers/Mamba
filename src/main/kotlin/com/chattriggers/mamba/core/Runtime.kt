package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.ast.nodes.expressions.Argument
import com.chattriggers.mamba.ast.nodes.expressions.DotAccessNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.values.collections.VString
import com.chattriggers.mamba.core.values.collections.VStringType
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.Wrapper
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.collections.*
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.numbers.*
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VNotImplemented
import com.chattriggers.mamba.core.values.singletons.VTrue

class Runtime(private val ctx: ThreadContext) {
    fun toBoolean(value: VObject): Boolean {
        return when {
            value is VNone || value is VFalse -> false
            value is VInt -> value.int != 0
            value is VFloat -> value.double != 0.0
            value is VComplex -> value.real != 0.0 || value.imag != 0.0
            value is VString -> value.string.isNotEmpty()
            value is VList -> value.list.isNotEmpty()
            value is VDict -> value.dict.isNotEmpty()
            value is VTuple -> value.items.isNotEmpty()
            value is VRange -> value.start.int == 0 && value.stop.int == 0 && value.step.int == 1
            value.containsSlot("__bool__") -> callProp(value, "__bool__") != VFalse
            value.containsSlot("__len__") -> toInt(callProp(value, "__len__")) != 0
            else -> true
        }
    }

    fun toInt(obj: VObject): Int {
        return when (obj) {
            is VInt -> obj.int
            else -> {
                val ret = callProp(obj, "__int__")
                if (ret !is VInt) TODO()
                return ret.int
            }
        }
    }

    fun toDouble(value: VObject): Double {
        TODO()
    }

    fun toVObject(obj: Any?): VObject = when (obj) {
        is MutableList<*> -> construct(VListType, listOf(obj))
        is List<*> -> construct(VTupleType, listOf(obj))
        is MutableMap<*, *> -> construct(VDictType, listOf(obj))
        is Int -> construct(VIntType, listOf(obj))
        is Double -> construct(VFloatType, listOf(obj))
        is Float -> construct(VFloatType, listOf(obj.toDouble()))
        is String -> construct(VStringType, listOf(obj))
        is Boolean -> if (obj) VTrue else VFalse
        null -> VNone
        else -> TODO("Conversion of type ${obj.javaClass.simpleName} to Value not implemented")
    }

    fun getName(value: Node): String {
        return when (value) {
            is IdentifierNode -> value.identifier
            is DotAccessNode -> value.property.identifier
            else -> TODO()
        }
    }

    fun valueCompare(method: String, left: VObject, right: VObject): VObject {
        return when {
            left.containsSlot(method) -> callProp(left, method, listOf(right))
            else -> VNotImplemented
        }
    }

    fun valueArithmetic(method: String, reverseMethod: String, left: VObject, right: VObject): VObject {
        return when {
            left.containsSlot(method) -> callProp(left, method, listOf(right))
            right.containsSlot(reverseMethod) -> callProp(right, reverseMethod, listOf(left))
            else -> VNotImplemented
        }
    }

    fun callProp(obj: VObject, property: String, args: List<Value> = emptyList()): VObject {
        return callPropWithArgs(obj, Wrapper(property), args.map {
            Argument(it, null, spread = false, kwSpread = false)
        })
    }

    fun callPropWithArgs(obj: VObject, property: Value, args: List<Argument> = emptyList()): VObject {
        val prop = obj.getValue(property)
        return call(prop, args)
    }

    fun call(obj: Any, args: List<Argument>): VObject {
        return when (obj) {
            is IMethod -> obj.call(ctx, args)
            is VObject -> {
                if (obj.containsSlot("__call__"))
                    return callPropWithArgs(obj, Wrapper("__call__"), args)

                VTypeError.construct("'${obj.className}' object is not callable")
            }
            else -> TODO("Error")
        }
    }

    fun construct(type: VType) = construct(type, emptyList())

    fun construct(type: VType, args: List<Any>): VObject {
        val mappedArgs = args.map {
            if (it !is Value) Wrapper(it) else it
        }
        val obj = callProp(type, "__new__", listOf(type) + mappedArgs)

        // TODO: Ensure VNone is returned
        callProp(obj, "__init__", mappedArgs)

        return obj
    }

    fun constructFromArgs(type: VType, args: List<Argument>): VObject {
        val obj = callPropWithArgs(type, Wrapper("__new__"), listOf(
            Argument(type, null, spread = false, kwSpread = false)) + args
        )

        // TODO: Ensure VNone is returned
        callPropWithArgs(obj, Wrapper("__init__"), args)

        return obj
    }

    fun getIterator(iterable: VObject): VObject {
        return callProp(iterable, "__iter__")
    }

    fun getIteratorNext(iterator: VObject): VObject {
        return callProp(iterator, "__next__")
    }

    fun isIterable(obj: VObject) = obj.containsSlot("__iter__")

    fun isIterator(obj: VObject) = isIterable(obj) && obj.containsSlot("__next__")

    fun iterableToList(iterable: VObject): Value {
        if (!isIterable(iterable)) TODO()

        val iterator = getIterator(iterable)
        val list = mutableListOf<VObject>()

        while (true) {
            when (val next = getIteratorNext(iterator)) {
                is VStopIteration -> return Wrapper(list)
                is VBaseException -> return next
                else -> list.add(next)
            }
        }
    }
}
