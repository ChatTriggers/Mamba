package com.chattriggers.mamba.core.values

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.functions.VNativeFunction

open class ClassDescriptor(val parent: ClassDescriptor? = null) : MutableMap<String, VObject> by mutableMapOf() {
    protected fun addClassProperty(name: String, value: VObject) {
        this[name] = value
    }
    
    protected fun addClassMethod(name: String, func: (interp: Interpreter, args: List<VObject>) -> VObject) {
        this[name] = VNativeFunction(name, func)
    }

    internal fun lookup(name: String): VObject? {
        if (name in this)
            return this[name]
        return parent?.lookup(name)
    }

    internal fun has(name: String): Boolean {
        return name in this || parent?.contains(name) ?: false
    }

    internal fun set(name: String, value: VObject): VObject? {
        if (name in this) {
            val ret = this[name]
            this[name] = value
            return ret
        }

        return parent?.set(name, value)
    }

    companion object {
        inline fun <reified T : VObject> assertSelf(args: List<VObject>): T {
            if (args.isEmpty() || args[0] !is T)
                TODO()
            return args[0] as T
        }

        inline fun <reified T : VObject> assertArg(args: List<VObject>, index: Int): T {
            if (index >= args.size || args[index] !is T)
                TODO()
            return args[index] as T
        }
    }
}