package com.chattriggers.mamba.ir.nodes

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Runtime
import com.chattriggers.mamba.core.values.*
import com.chattriggers.mamba.core.values.numbers.toValue

sealed class ExpressionNode(children: List<Node>) : Node(children) {
    constructor(child: Node) : this(listOf(child))

    constructor() : this(emptyList())
}

class OrExpresionNode(
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class AndExpressionNode(
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        right.print(indent + 1)
    }
}

class NotExpressionNode(private val child: ExpressionNode) : ExpressionNode(child) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        child.print(indent + 1)
    }
}

class ComparisonNode(
    private val op: ComparisonOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        printIndent(indent)
        println(op.symbol)
        right.print(indent + 1)
    }
}

class ArithmeticExpressionNode(
    private val op: ArithmeticOperator,
    private val left: ExpressionNode,
    private val right: ExpressionNode
) : ExpressionNode(listOf(left, right)) {
    override fun execute(interp: Interpreter): Value {
        return Runtime.add(left.execute(interp), right.execute(interp))
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        left.print(indent + 1)
        printIndent(indent + 1)
        println(op.symbol)
        right.print(indent + 1)
    }
}

class UnaryExpressionNode(
    private val op: UnaryOperator,
    private val child: ExpressionNode
) : ExpressionNode(child) {
    override fun execute(interp: Interpreter): Value {
        val value = child.execute(interp)

        return when (op) {
            UnaryOperator.NEG -> value.callFunction(interp, "__neg__")
            UnaryOperator.POS -> value.callFunction(interp, "__pos__")
            UnaryOperator.INVERT -> value.callFunction(interp, "__invert__")
        }
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        printIndent(indent + 1)
        println(op.symbol)
        child.print(indent + 1)
    }
}

class IdentifierNode(val identifier: String) : ExpressionNode() {
    override fun execute(interp: Interpreter): Value {
        for (scope in interp.scopeStack) {
            val result = scope[identifier]
            if (result != null)
                return result
        }

        TODO()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$identifier\"")
    }
}

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

abstract class Literal : ExpressionNode()

class StringLiteral(private val string: String) : Literal() {
    override fun execute(interp: Interpreter): Value {
        return VString(string)
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" $string")
    }
}

// TODO: Number formatting
class NumberLiteral(private val num: Int) : Literal() {
    override fun execute(interp: Interpreter) = num.toValue()

    override fun print(indent: Int) {
        printNodeHeader(indent, this, newLine = false)
        println(" \"$num\"")
    }
}

class FunctionCallNode(
    private val target: ExpressionNode,
    private val args: List<ExpressionNode>
) : ExpressionNode(listOf(target) + args) {
    override fun execute(interp: Interpreter): Value {
        val targetValue = target.execute(interp)
        var callable: ICallable? = null

        if (targetValue is ICallable)
            callable = targetValue

        if (targetValue is VWrapper && targetValue.value is ICallable)
            callable = targetValue.value

        if (callable == null)
            TODO()

        return callable.call(interp, args.map { it.execute(interp) })
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        args.forEach { it.print(indent + 1) }
    }
}

class MemberAccessNode(
    private val target: ExpressionNode,
    private val members: List<ExpressionNode>)
    : ExpressionNode(listOf(target) + members) {
    override fun execute(interp: Interpreter): Value {
        TODO()
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        members.forEach { it.print(indent + 1) }
    }
}

class DotAccessNode(
    private val target: ExpressionNode,
    private val property: IdentifierNode
) : ExpressionNode(listOf(target, property)) {
    override fun execute(interp: Interpreter): Value {
        return target.execute(interp)[property.identifier] ?: VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        target.print(indent + 1)
        property.print(indent + 1)
    }
}
