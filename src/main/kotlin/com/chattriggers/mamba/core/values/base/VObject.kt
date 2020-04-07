package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.collections.toValue
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VAttributeError
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.exceptions.notImplemented
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.toValue

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

    internal val slotMap = mutableMapOf<VObject, Slot>()

    fun getValue(key: String) = getValue(key.toValue())

    fun getValue(key: VObject): VObject {
        return getSlot(key).value
    }

    fun getSlot(key: String) = getSlot(key.toValue())

    fun getSlot(key: VObject): Slot {
        ensureUnwrapped()

        if (key in slotMap)
            return slotMap[key]!!

        for (baseType in baseTypesUnwrapped) {
            if (key in baseType.slotMap)
                return baseType.slotMap[key]!!
        }

        throw MambaException(VAttributeError(key.toString(), className))
    }

    fun containsSlot(key: String) = containsSlot(key.toValue())

    fun containsSlot(key: VObject) = try {
        getSlot(key)
        true
    } catch (e: MambaException) {
        if (e.reason is VAttributeError) {
            false
        } else {
            throw e
        }
    }

    fun putSlot(key: String, value: VObject, createNewSlot: Boolean = true) {
        putSlot(key.toValue(), value, createNewSlot)
    }

    fun putSlot(key: VObject, value: VObject, createNewSlot: Boolean = true) {
        ensureUnwrapped()

        when {
            containsSlot(key) -> getSlot(key).value = value
            createNewSlot -> slotMap[key] = Slot(key, value, isStatic = false, isWritable = true)
            else -> throw IllegalStateException("No existing slot for key $key")
        }
    }

    fun allKeys() = slotMap.keys + baseTypes.map { it.valueProducer().slotMap.keys }.flatten()

    protected fun ensureUnwrapped() {
        if (baseTypes.isNotEmpty() && baseTypesUnwrapped.isEmpty()) {
            baseTypes.map { it.valueProducer() }.run(baseTypesUnwrapped::addAll)

            for (baseType in baseTypesUnwrapped) {
                baseType.ensureUnwrapped()

                for ((key, slot) in baseType.slotMap) {
                    // TODO: We only move methods into this.slotMap
                    // since fields don't need to be bound. Is this
                    // an issue?
                    if (key in slotMap) continue

                    if (slot.isStatic) continue

                    val v = slot.value
                    if (v !is IMethod) continue

                    slotMap[key] = slot.copy(valueBacker = v.bind(this))
                }
            }
        }
    }
}

object VObjectType : VType() {
    init {
        addField("__class__", VTypeType)

        addMethod("__dir__") {
            assertSelfAs<VObject>().allKeys().toList().toValue()
        }

        addField("__doc__", "The most base type".toValue())

        addMethod("__getattribute__") {
            val self = assertSelfAs<VObject>()
            val key = assertArgAs<VObject>(1)

            when {
                self.containsSlot(key) -> self.getValue(key)
                self.containsSlot("__getattr__") -> runtime.callProperty(self, "__getattr__", listOf(self, key))
                else -> throw MambaException(VAttributeError(key.toString(), self.className))
            }

            // TODO: Property descriptors via __get__ and __set__
        }

        addMethod("__str__") {
            assertSelfAs<VObject>().toString().toValue()
        }

        addMethod("__call__", id = "obj_call") {
            runtime.construct(VObjectType, arguments())
        }

        addMethod("__new__", isStatic = true) {
            val type = assertArgAs<VType>(0)

            if (type !is VObjectType) {
                val name = type.className
                throw MambaException(VTypeError("object.__new__($name) is not safe, use $name.__new__()"))
            }

            VObject(LazyValue("VObjectType") { VObjectType })
        }

        addMethod("__init__") {
            VNone
        }
    }
}
