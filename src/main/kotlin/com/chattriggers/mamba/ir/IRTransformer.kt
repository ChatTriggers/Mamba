package com.chattriggers.mamba.ir

import com.chattriggers.mamba.generated.Python3Parser
import com.chattriggers.mamba.ir.nodes.ScriptNode

internal object IRTransformer {
    fun transform(tree: Python3Parser.FileInputContext): ScriptNode {
        val visitor = IRVisitor()
        return visitor.visitFileInput(tree)
    }
}