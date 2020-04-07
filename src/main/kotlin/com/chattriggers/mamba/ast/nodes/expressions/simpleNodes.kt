package com.chattriggers.mamba.ast.nodes.expressions

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.singletons.VEllipsis
import com.chattriggers.mamba.core.values.singletons.VFalse
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.singletons.VTrue

class EllipsisNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject = VEllipsis

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class TrueNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject = VTrue

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class FalseNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject = VFalse

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}

class NoneNode(lineNumber: Int) : ExpressionNode(lineNumber) {
    override fun execute(interp: Interpreter): VObject = VNone

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
    }
}
