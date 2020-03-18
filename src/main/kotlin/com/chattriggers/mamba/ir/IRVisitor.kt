package com.chattriggers.mamba.ir

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.generated.Python3Parser.*
import com.chattriggers.mamba.ir.nodes.*
import org.antlr.v4.runtime.tree.TerminalNode

internal class IRVisitor {
    fun visitFileInput(ctx: FileInputContext): ScriptNode {
        return ScriptNode(ctx.statement().map(this::visitStatement))
    }

    private fun visitStatement(ctx: StatementContext): StatementNode {
        val simple = ctx.simpleStatement()
        return if (simple != null) {
            visitSimpleStatement(simple)
        } else {
            visitCompoundStatement(ctx.compoundStatement())
        }
    }

    private fun visitSimpleStatement(ctx: SimpleStatementContext): StatementNode {
        return ctx.smallStatement().map(::visitSmallStatement)
            .map { it.children }
            .flatten()
            .let(::StatementNode)
    }

    private fun visitSmallStatement(ctx: SmallStatementContext): StatementNode {
        val exprStatement = ctx.exprStatement()
        if (exprStatement != null) {
            return StatementNode(visitExprStatement(exprStatement))
        }

        val flowStatement = ctx.flowStatement()
        if (flowStatement != null)
            return StatementNode(visitFlowStatement(flowStatement))

        TODO("Handle other branch possibilities")
    }

    private fun visitFlowStatement(ctx: FlowStatementContext): StatementNode {
        val returnStatement = ctx.returnStatement()

        if (returnStatement != null) {
            val testList = returnStatement.testList()
            val tests = testList.test()

            if (tests.size > 1) {
                TODO("Handle other branch possibilities")
            } else {
                return ReturnNode(visitTest(tests[0]))
            }
        }

        TODO("Handle other branch possibilities")
    }

    private fun visitCompoundStatement(ctx: CompoundStatementContext): StatementNode {
        val functionCtx = ctx.funcDef()
        if (functionCtx != null)
            return StatementNode(visitFuncDef(functionCtx))

        TODO("Handle other possibilities")
    }

    private fun visitFuncDef(ctx: FuncDefContext): FunctionNode {
        // TODO: Can this ever be null? Not sure if all functions have
        // to be named given that Python provides explicit lambda support
        val name = ctx.NAME().text
        val typedArgsList = ctx.typedArgsList()
        val test = ctx.test() // TODO: What is this?

        val suite = ctx.suite()

        if (typedArgsList != null) {
            TODO("Handle other possibilities")
        } else if (test != null) {
            TODO("Handle other possibilities")
        }

        val statements = mutableListOf<StatementNode>()

        val simpleStatement = suite.simpleStatement()

        if (simpleStatement != null) {
            statements.add(visitSimpleStatement(simpleStatement))
        } else {
            statements.addAll(suite.statement().map(::visitStatement))
        }

        return FunctionNode(IdentifierNode(name), statements)
    }

    private fun visitExprStatement(ctx: ExprStatementContext): List<ExpressionNode> {
        val testListStarExpr = ctx.testlistStarExpression()

        if (testListStarExpr.size > 1) {
            TODO("Handle other possibilities")
        }

        return visitTestListStarExpr(testListStarExpr[0])
    }

    private fun visitTestListStarExpr(ctx: TestlistStarExpressionContext): List<ExpressionNode> {
        val starExpr = ctx.starExpression()
        if (starExpr.isNotEmpty()) {
            TODO("Handle other possibilities")
        }

        val testExpr = ctx.test()
        if (testExpr.size > 1) {
            TODO("Handle other possibilities")
        }

        // TODO: CommaNode
        return listOf(visitTest(testExpr[0]))
    }

    private fun visitTest(ctx: TestContext): ExpressionNode {
        val lambdaDef = ctx.lambdaDef()

        if (lambdaDef != null) {
            TODO("Handle other possibilities")
        }

        val orTest = ctx.orTest()

        if (orTest.size == 1) {
            return visitOrTest(orTest[0])
        } else {
            // Ternary statement
            TODO("Handle other possibilities")
        }
    }

    private fun visitOrTest(ctx: OrTestContext): ExpressionNode {
        // If there is just one AndTest, then it isn't really
        // an OrTest at all, just immediately delegate to AndTest.
        // If there is more than one, then they are all or'd
        // together

        val andTests = ctx.andTest().map(::visitAndTest)

        return if (andTests.size == 1) {
            andTests[0]
        } else {
            andTests.reduce(::OrExpresionNode)
        }
    }

    private fun visitAndTest(ctx: AndTestContext): ExpressionNode {
        val notTests = ctx.notTest().map(::visitNotTest)

        return if (notTests.size == 1) {
            notTests[0]
        } else {
            notTests.reduce(::AndExpressionNode)
        }
    }

    private fun visitNotTest(ctx: NotTestContext): ExpressionNode {
        val notTest = ctx.notTest()

        return if (notTest != null) {
            NotExpressionNode(visitNotTest(notTest))
        } else {
            visitComparison(ctx.comparison())
        }
    }

    private fun visitComparison(ctx: ComparisonContext): ExpressionNode {
        val expressions = ctx.expression()

        return if (expressions.size == 1) {
            visitExpression(expressions[0])
        } else {
            val operators = ctx.compOperator()

            if (operators.size != expressions.size - 1) {
                codeBug()
            }

            // TODO: Verify this runs in the correct order of precedence

            var compNode = ComparisonNode(
                ComparisonOperator.from(operators[0].text),
                visitExpression(expressions[0]),
                visitExpression(expressions[1])
            )

            operators.drop(1).forEachIndexed { index, compCtx ->
                compNode = ComparisonNode(
                    ComparisonOperator.from(compCtx.text),
                    compNode,
                    visitExpression(expressions[index + 2])
                )
            }

            return compNode
        }
    }

    private fun visitExpression(ctx: ExpressionContext): ExpressionNode {
        val bitXorNodes = ctx.bitXorExpr().map(::visitBitXorExpr)

        return if (bitXorNodes.size == 1) {
            bitXorNodes[0]
        } else {
            bitXorNodes.reduce { prev, curr ->
                ArithmeticExpressionNode(
                    ArithmeticOperator.BIT_OR,
                    prev,
                    curr
                )
            }
        }
    }

    private fun visitBitXorExpr(ctx: BitXorExprContext): ExpressionNode {
        val bitAndNodes = ctx.bitAndExpr().map(::visitBitAndExpr)

        return if (bitAndNodes.size == 1) {
            bitAndNodes[0]
        } else {
            bitAndNodes.reduce { prev, curr ->
                ArithmeticExpressionNode(
                    ArithmeticOperator.BIT_XOR,
                    prev,
                    curr
                )
            }
        }
    }

    private fun visitBitAndExpr(ctx: BitAndExprContext): ExpressionNode {
        val bitShiftNodes = ctx.bitShiftExpr().map(::visitBitShiftTest)

        return if (bitShiftNodes.size == 1) {
            bitShiftNodes[0]
        } else {
            bitShiftNodes.reduce { prev, curr ->
                ArithmeticExpressionNode(
                    ArithmeticOperator.BIT_AND,
                    prev,
                    curr
                )
            }
        }
    }

    private fun visitBitShiftTest(ctx: BitShiftExprContext): ExpressionNode {
        val arithmeticExprs = ctx.arithmeticExpr()

        return if (arithmeticExprs.size == 1) {
            visitArithmeticExpr(arithmeticExprs[0])
        } else {
            val operators = ctx.bitShiftOperator()

            if (operators.size != arithmeticExprs.size - 1) {
                codeBug()
            }

            // TODO: Verify this runs in the correct order of precedence

            var bitShiftNode = ArithmeticExpressionNode(
                ArithmeticOperator.from(operators[0].text),
                visitArithmeticExpr(arithmeticExprs[0]),
                visitArithmeticExpr(arithmeticExprs[1])
            )

            operators.drop(1).forEachIndexed { index, bitShiftCtx ->
                bitShiftNode = ArithmeticExpressionNode(
                    ArithmeticOperator.from(bitShiftCtx.text),
                    bitShiftNode,
                    visitArithmeticExpr(arithmeticExprs[index + 2])
                )
            }

            return bitShiftNode
        }
    }

    private fun visitArithmeticExpr(ctx: ArithmeticExprContext): ExpressionNode {
        val terms = ctx.term()

        return if (terms.size == 1) {
            visitTerm(terms[0])
        } else {
            val operators = ctx.arithmeticOperator()

            if (operators.size != terms.size - 1) {
                codeBug()
            }

            // TODO: Verify this runs in the correct order of precedence

            var arithNode = ArithmeticExpressionNode(
                ArithmeticOperator.from(operators[0].text),
                visitTerm(terms[0]),
                visitTerm(terms[1])
            )

            operators.drop(1).forEachIndexed { index, arithCtx ->
                arithNode = ArithmeticExpressionNode(
                    ArithmeticOperator.from(arithCtx.text),
                    arithNode,
                    visitTerm(terms[index + 2])
                )
            }

            return arithNode
        }
    }

    private fun visitTerm(ctx: TermContext): ExpressionNode {
        val factors = ctx.factor()

        return if (factors.size == 1) {
            visitFactor(factors[0])
        } else {
            val operators = ctx.termOperator()

            if (operators.any { it.text == "@" }) {
                TODO()
            }

            if (operators.size != factors.size - 1) {
                codeBug()
            }

            // TODO: Verify this runs in the correct order of precedence

            var arithNode = ArithmeticExpressionNode(
                ArithmeticOperator.from(operators[0].text),
                visitFactor(factors[0]),
                visitFactor(factors[1])
            )

            operators.drop(1).forEachIndexed { index, termCtx ->
                arithNode = ArithmeticExpressionNode(
                    ArithmeticOperator.from(termCtx.text),
                    arithNode,
                    visitFactor(factors[index + 2])
                )
            }

            return arithNode
        }
    }

    private fun visitFactor(ctx: FactorContext): ExpressionNode {
        val power = ctx.power()

        if (power != null) {
            return visitPower(power)
        }

        val factor = ctx.factor()
        val op = ctx.factorOperator()

        return UnaryExpressionNode(
            UnaryOperator.from(op.text),
            visitFactor(factor)
        )
    }

    private fun visitPower(ctx: PowerContext): ExpressionNode {
        val atomExpr = ctx.atomExpression()
        val factor = ctx.factor()

        return if (factor != null) {
            ArithmeticExpressionNode(
                ArithmeticOperator.POWER,
                visitAtomExpr(atomExpr),
                visitFactor(factor)
            )
        } else {
            visitAtomExpr(atomExpr)
        }
    }

    private fun visitAtomExpr(ctx: AtomExpressionContext): ExpressionNode {
        val await = ctx.AWAIT()
        val atom = ctx.atom()
        val trailers = ctx.trailer()

        if (await != null) {
            TODO()
        }

        val atomNode = visitAtom(atom)

        return if (trailers.isEmpty()) {
            atomNode
        } else {
            makeTrailers(atomNode, trailers)
        }
    }

    private fun visitAtom(ctx: AtomContext): ExpressionNode {
        val parenAtom = ctx.parenAtom()
        if (parenAtom != null)
            return visitParenAtom(parenAtom)

        val listAtom = ctx.listAtom()
        if (listAtom != null)
            return visitListAtom(listAtom)

        val dictOrSetAtom = ctx.dictOrSetAtom()
        if (dictOrSetAtom != null)
            return visitDictOrSetAtom(dictOrSetAtom)

        return visitBasicAtom(ctx.basicAtom())
    }

    private fun visitParenAtom(ctx: ParenAtomContext): ExpressionNode {
        TODO()
    }

    private fun visitListAtom(ctx: ListAtomContext): ExpressionNode {
        TODO()
    }

    private fun visitDictOrSetAtom(ctx: DictOrSetAtomContext): ExpressionNode {
        TODO()
    }

    private fun visitBasicAtom(ctx: BasicAtomContext): ExpressionNode {
        return ctx.NAME()?.let { IdentifierNode(it.text) } ?:
                ctx.NUMBER()?.let { makeNumber(it.text) } ?:
                ctx.ELLIPSIS()?.let { EllipsisNode } ?:
                ctx.NONE()?.let { NoneNode } ?:
                ctx.TRUE()?.let { TrueNode } ?:
                ctx.FALSE()?.let { FalseNode } ?:
                ctx.STRING().let(this::makeStrings)
    }

    private fun makeTrailers(atomNode: ExpressionNode, trailers: List<TrailerContext>): ExpressionNode {
        var node = atomNode

        for (trailer in trailers) {
            val call = trailer.trailerCall()

            if (call != null) {
                node = FunctionCallNode(node, visitArgList(call.argList()))
                continue
            }

            val memberAccess = trailer.trailerMemberAccess()
            if (memberAccess != null) {
                node = MemberAccessNode(node, visitSubscriptList(memberAccess.subscriptList()))
                continue
            }

            val dotAccess = trailer.trailerDotAccess()
            node = DotAccessNode(node, IdentifierNode(dotAccess.NAME().text))
        }

        return node
    }

    private fun visitArgList(ctx: ArgListContext?): List<ExpressionNode> {
        if (ctx == null)
            return emptyList()

        val args = ctx.argument()
        if (args.isEmpty())
            return emptyList()

        return args.map(::visitArgument)
    }

    private fun visitArgument(ctx: ArgumentContext): ExpressionNode {
        if (ctx.argumentNamed() != null || ctx.argumentKwargSpread() != null || ctx.argumentArgSpread() != null)
            TODO()

        val arg = ctx.argumentCompFor()

        if (arg.compFor() != null)
            TODO()

        return visitTest(arg.test())
    }

    private fun visitSubscriptList(ctx: SubscriptListContext?): List<ExpressionNode> {
        if (ctx == null)
            return emptyList()

        TODO()
    }

    private fun makeNumber(number: String): ExpressionNode {
        // TODO: Number formatting
        return NumberLiteral(number.toInt())
    }

    private fun makeStrings(strings: List<TerminalNode>): ExpressionNode {
        return StringLiteral(strings.joinToString { it.text })
    }

    private fun codeBug(): Nothing {
        throw IllegalStateException()
    }
}
