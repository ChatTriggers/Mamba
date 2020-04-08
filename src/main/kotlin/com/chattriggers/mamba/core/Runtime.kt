package com.chattriggers.mamba.core

import com.chattriggers.mamba.ast.nodes.Node
import com.chattriggers.mamba.ast.nodes.expressions.DotAccessNode
import com.chattriggers.mamba.ast.nodes.expressions.IdentifierNode
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.VDict
import com.chattriggers.mamba.core.values.collections.VList
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.base.IMethod
import com.chattriggers.mamba.core.values.numbers.*
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.base.VType
import com.chattriggers.mamba.core.values.base.Wrapper
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VNotImplemented
import com.chattriggers.mamba.core.values.singletons.toValue

class Runtime(val interp: Interpreter) {
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

    fun toInt(value: VObject): Int {
        notImplemented()
    }

    fun toDouble(value: VObject): Double {
        notImplemented()
    }

    fun getName(value: Node): String {
        return when (value) {
            is IdentifierNode -> value.identifier
            is DotAccessNode -> value.property.identifier
            else -> notImplemented()
        }
    }

    fun widen(left: VObject, right: VObject): Pair<VObject, VObject> {
        return widenHelper(left, right) to widenHelper(right, left)
    }

    private fun widenHelper(self: VObject, other: VObject): VObject {
        if (self !is VInt && self !is VFloat && self !is VComplex)
            notImplemented()

        if (other !is VInt && other !is VFloat && other !is VComplex)
            notImplemented()

        return when (other) {
            is VComplex -> when (self) {
                is VComplex -> self
                is VFloat -> VComplex(
                    self.double,
                    0.0
                )
                is VInt -> VComplex(
                    self.int.toDouble(),
                    0.0
                )
                else -> notImplemented()
            }
            is VFloat -> when (self) {
                is VComplex, is VFloat -> self
                is VInt -> VFloat(
                    self.int.toDouble()
                )
                else -> notImplemented()
            }
            is VInt -> self
            else -> notImplemented()
        }
    }

    fun callProperty(obj: VObject, property: String, args: List<Value> = emptyList()): VObject {
        return callProperty(obj, property.toValue(), args)
    }

    fun callProperty(obj: VObject, property: VObject, args: List<Value> = emptyList()): VObject {
        val prop = obj.getValue(property)
        return call(prop, args)
    }

    fun call(obj: VObject, args: List<Value>): VObject {
        if (obj is IMethod)
            return obj.call(interp, args)

        if (obj.containsSlot("__call__"))
            return callProperty(obj, "__call__", args)

        throw MambaException(VTypeError("'${obj.className}' object is not callable"))
    }

    fun construct(type: VType) = construct(type, emptyList())

    fun construct(type: VType, args: List<Any>): VObject {
        val mappedArgs = args.map {
            if (it !is Value) Wrapper(it) else it
        }
        val obj = callProperty(type, "__new__", listOf(type) + mappedArgs)

        // TODO: May have to pass obj with args depending on implementation
        // of __new__ and __init__
        // TODO: Ensure VNone is returned
        callProperty(obj, "__init__", mappedArgs)

        return obj
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

    fun getIterator(iterable: VObject): VObject {
        return callProperty(iterable, "__iter__")
    }

    fun getIterableNext(iterator: VObject): VObject {
        return callProperty(iterator, "__next__")
    }

    companion object {
        fun toValue(obj: Any): VObject {
            return when (obj) {
                is Int -> obj.toValue()
                is String -> obj.toValue()
                is Boolean -> obj.toValue()
                else -> throw IllegalArgumentException()
            }
        }

        fun isIterable(obj: VObject) = obj.containsSlot("__iter__")

        fun isIterator(obj: VObject) = isIterable(obj) && obj.containsSlot("__next__")
    }
}
