package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.IMethod
import com.chattriggers.mamba.core.Slot
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.exceptions.VAttributeError
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.singletons.VNone

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
open class VObject(private vararg val baseTypes: LazyValue<VType>) : Value {
    private val baseTypesUnwrapped = mutableListOf<VType>()

    open val className = "object"

    internal val slotMap = mutableMapOf<Value, Slot>()

    fun getValue(key: String) = getValue(Wrapper(key))

    fun getValue(key: Value): Value {
        return getSlot(key).value
    }

    fun getSlot(key: String) = getSlot(Wrapper(key))

    fun getSlot(key: Value): Slot {
        ensureUnwrapped()

        if (key in slotMap)
            return slotMap[key]!!

        for (baseType in baseTypesUnwrapped) {
            baseType.ensureUnwrapped()

            if (baseType.containsSlot(key)) {
                val slot = baseType.getSlot(key)
                val value = slot.value

                if (!slot.isStatic && value is IMethod) {
                    val newSlot = slot.copy(valueBacker = value.bind(this))
                    slotMap[key] = newSlot
                    return newSlot
                }

                return slot
            }
        }

        throw IllegalStateException("Object $className has no slot with name $key")
    }

    fun containsSlot(key: String) = containsSlot(Wrapper(key))

    fun containsSlot(key: Value) = try {
        getSlot(key)
        true
    } catch (e: IllegalStateException) {
        false
    }

    fun putSlot(key: String, value: Value, createNewSlot: Boolean = true) {
        putSlot(Wrapper(key), value, createNewSlot)
    }

    fun putSlot(key: Value, value: Value, createNewSlot: Boolean = true) {
        ensureUnwrapped()

        when {
            containsSlot(key) -> getSlot(key).value = value
            createNewSlot -> slotMap[key] =
                Slot(key, value, isStatic = false, isWritable = true)
            else -> throw IllegalStateException("No existing slot for key $key")
        }
    }

    fun allKeys() = slotMap.keys + baseTypes.map { it.valueProducer().slotMap.keys }.flatten()

    protected fun ensureUnwrapped() {
        if (baseTypes.isNotEmpty() && baseTypesUnwrapped.isEmpty()) {
            baseTypes.map { it.valueProducer() }.run(baseTypesUnwrapped::addAll)
        }
    }
}

object VObjectType : VType() {
    init {
        addField("__class__") {
            VTypeType
        }

        addMethod("__dir__") {
            assertSelfAs<VObject>().allKeys().toList().map {
                when (it) {
                    is VObject -> it
                    is Wrapper -> it.toValue()
                    else -> TODO("Error")
                }
            }.toValue()
        }

        addField("__doc__") {
            "The most base type".toValue()
        }

        addMethod("__getattribute__") {
            val self = assertSelfAs<VObject>()
            val key = assertArgAs<VObject>(1)

            when {
                self.containsSlot(key) -> self.getValue(key).unwrap()
                self.containsSlot("__getattr__") -> runtime.callProp(self, "__getattr__", listOf(self, key))
                else -> VExceptionWrapper(VAttributeError.construct(key.toString(), self.className))
            }

            // TODO: Property descriptors via __get__ and __set__
        }

        addMethod("__str__") {
            assertSelfAs<VObject>().toString().toValue()
        }

        addMethod("__call__", id = "obj_call") {
            runtime.construct(VObjectType, argumentValuesRaw())
        }

        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VObjectType) {
                val name = type.className
                return@addMethod VExceptionWrapper(VTypeError.construct("object.__new__($name) is not safe, use $name.__new__()"))
            }

            VObject(LazyValue("VObjectType") { VObjectType })
        }

        addMethod("__init__") {
            VNone
        }
    }
}
