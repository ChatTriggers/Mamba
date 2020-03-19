package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.*

object EllipsisNode : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject = VEllipsis

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object TrueNode : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject = VTrue

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object FalseNode : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject = VFalse

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object NoneNode : ExpressionNode() {
    override fun execute(interp: Interpreter): VObject = VNone

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}
