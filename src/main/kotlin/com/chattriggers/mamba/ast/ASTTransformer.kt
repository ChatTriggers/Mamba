package com.chattriggers.mamba.ast

import com.chattriggers.mamba.generated.Python3Parser
import com.chattriggers.mamba.ast.nodes.ScriptNode

internal object ASTTransformer {
    fun transform(tree: Python3Parser.FileInputContext): ScriptNode {
        val visitor = ASTVisitor()
        return visitor.visitFileInput(tree)
    }
}