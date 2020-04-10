package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.ast.nodes.expressions.DotAccessNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.base.*
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.core.values.collections.VListType
import com.chattriggers.mamba.core.values.collections.VTupleType
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import com.chattriggers.mamba.core.values.exceptions.VStopIteration
import com.chattriggers.mamba.core.values.numbers.*
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.singletons.*

class Runtime(private val ctx: ThreadContext) {
    fun toBoolean(value: VObject): Boolean {
        return when {
            value is VNone || value is VFalse -> false
            value is VInt -> value.int != 0
            value is VString -> value.string.isNotEmpty()
            value is VList -> value.list.isNotEmpty()
            value is VDict -> value.dict.isNotEmpty()
            value.containsSlot("__bool__") -> toBoolean(callProperty(value, "__bool__"))
            value.containsSlot("__len__") -> toInt(callProperty(value, "__len__")) != 0
            else -> true
        }
    }

    fun toInt(obj: VObject): Int {
        return when (obj) {
            is VInt -> obj.int
            else -> {
                val ret = callProperty(obj, "__int__")
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
            left.containsSlot(method) -> callProperty(left, method, listOf(right))
            else -> VNotImplemented
        }
    }

    fun valueArithmetic(method: String, reverseMethod: String, left: VObject, right: VObject): VObject {
        return when {
            left.containsSlot(method) -> callProperty(left, method, listOf(right))
            right.containsSlot(reverseMethod) -> callProperty(right, reverseMethod, listOf(left))
            else -> VNotImplemented
        }
    }

    fun callProperty(obj: VObject, property: String, args: List<Value> = emptyList()): VObject {
        return callProperty(obj, Wrapper(property), args)
    }

    fun callProperty(obj: VObject, property: Value, args: List<Value> = emptyList()): VObject {
        val prop = obj.getValue(property)
        return call(prop, args)
    }

    fun call(obj: Any, args: List<Value>): VObject {
        return when (obj) {
            is IMethod -> obj.call(ctx, args)
            is VObject -> {
                if (obj.containsSlot("__call__"))
                    return callProperty(obj, "__call__", args)

                VExceptionWrapper(VTypeError.construct("'${obj.className}' object is not callable"))
            }
            else -> TODO("Error")
        }
    }

    fun construct(type: VType) = construct(type, emptyList())

    fun construct(type: VType, args: List<Any>): VObject {
        val mappedArgs = args.map {
            if (it !is Value) Wrapper(it) else it
        }
        val obj = callProperty(type, "__new__", listOf(type) + mappedArgs)

        // TODO: Ensure VNone is returned
        callProperty(obj, "__init__", mappedArgs)

        return obj
    }

    fun getIterator(iterable: VObject): VObject {
        return callProperty(iterable, "__iter__")
    }

    fun getIteratorNext(iterator: VObject): VObject {
        return callProperty(iterator, "__next__")
    }

    fun isIterable(obj: VObject) = obj.containsSlot("__iter__")

    fun isIterator(obj: VObject) = isIterable(obj) && obj.containsSlot("__next__")

    fun iterableToList(iterable: VObject): Value {
        if (!isIterable(iterable)) TODO()

        val iterator = getIterator(iterable)
        val list = mutableListOf<VObject>()

        while (true) {
            val next = getIteratorNext(iterator)
            if (next is VExceptionWrapper) {
                if (next.exception is VStopIteration) {
                    return Wrapper(list)
                }
                return next
            }
            list.add(next)
        }
    }

    companion object {
        inline fun <reified T : VBaseException> isException(obj: VObject): Boolean {
            return obj is VExceptionWrapper && obj.exception is T
        }
    }
}
