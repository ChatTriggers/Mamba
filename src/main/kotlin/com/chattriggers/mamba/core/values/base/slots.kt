package com.chattriggers.mamba.core.values.base

import com.chattriggers.mamba.ast.nodes.statements.FunctionNode
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.LazyValue
import com.chattriggers.mamba.core.values.Value
import com.chattriggers.mamba.core.values.exceptions.MambaException
import com.chattriggers.mamba.core.values.exceptions.VTypeError
import com.chattriggers.mamba.core.values.unwrap

data class Slot(
    val key: VObject,
    private var valueBacker: VObject,
    val isStatic: Boolean,
    val isWritable: Boolean,
    val id: String? = null
) {
    var value: VObject
        get() = valueBacker
        set(value) {
            if (!isWritable) {
                throw MambaException(VTypeError("can't set write-only property '$key'"))
            }

            valueBacker = value
        }
}

class VNativeMethod(
    val method: NativeClassMethod,
    override var self: VObject? = null
) : VObject(LazyValue("VNativeMethodType") { VNativeMethodType }), IMethod {
    override fun bind(newSelf: VObject): VObject {
        return VNativeMethod(method, newSelf)
    }

    override fun call(ctx: ThreadContext, args: List<Value>): VObject {
        return when (val s = self) {
            null -> method(ClassMethodBuilder(ctx, args))
            else -> method(ClassMethodBuilder(ctx, listOf(s) + args))
        }
    }
}

object VNativeMethodType : VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            assertSelfAs<VNativeMethod>().call(ctx, arguments())
        }
    }
}

class VMethod(
    val method: FunctionNode,
    override var self: VObject? = null
) : VObject(LazyValue("VMethodType") { VMethodType }), IMethod {

    override fun bind(newSelf: VObject): VObject {
        return VMethod(method, newSelf)
    }

    override fun call(ctx: ThreadContext, args: List<Value>): VObject {
        return when (val s = self) {
            null -> method.call(ctx, args.map(Value::unwrap))
            else -> method.call(ctx, listOf(s) + args.map(Value::unwrap))
        }
    }
}

object VMethodType: VType(LazyValue("VObjectType") { VObjectType }) {
    init {
        addMethod("__call__") {
            assertSelfAs<VMethod>().method.call(ctx, arguments())
        }
    }
}
