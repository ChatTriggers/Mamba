package com.chattriggers.mamba.ir.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.VEllipsis
import com.chattriggers.mamba.core.values.numbers.VFalse
import com.chattriggers.mamba.core.values.VNone
import com.chattriggers.mamba.core.values.numbers.VTrue

object EllipsisNode : ExpressionNode() {
    override fun execute(interp: Interpreter) = VEllipsis

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object TrueNode : ExpressionNode() {
    override fun execute(interp: Interpreter) = VTrue

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object FalseNode : ExpressionNode() {
    override fun execute(interp: Interpreter) = VFalse

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

object NoneNode : ExpressionNode() {
    override fun execute(interp: Interpreter) = VNone

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}
