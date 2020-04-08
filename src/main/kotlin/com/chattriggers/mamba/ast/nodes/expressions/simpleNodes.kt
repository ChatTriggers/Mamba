package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.VEllipsis
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VTrue

class EllipsisNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject = VEllipsis

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class TrueNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject = VTrue

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class FalseNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject = VFalse

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class NoneNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(ctx: ThreadContext): VObject = VNone

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}
