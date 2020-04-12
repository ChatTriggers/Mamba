package com.chattriggers.mamba.ast.nodes.statements

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.VModule
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.VNone

class ImportNode(
    lineNumber: Int,
    val name: String
) : StatementNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject {
        val scope = ctx.interp.scopes.currScope
        val nativeModule = VModule.nativeModules.first { it.name == name }
        scope.putSlot(name, nativeModule)
        return VNone
    }
}