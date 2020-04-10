package com.chattriggers.mamba.ast

import com.chattriggers.mamba.api.ArithmeticOperator
import com.chattriggers.mamba.api.ComparisonOperator
import com.chattriggers.mamba.api.UnaryOperator
import com.chattriggers.mamba.generated.Python3Parser.*
import com.chattriggers.mamba.ast.nodes.*
import com.chattriggers.mamba.ast.nodes.expressions.*
import com.chattriggers.mamba.ast.nodes.expressions.literals.*
import com.chattriggers.mamba.ast.nodes.statements.*
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.TerminalNode

internal class ASTVisitor {
    fun ParserRuleContext.lineNumber() = this.start.line

    fun visitFileInput(ctx: FileInputContext): ScriptNode {
        return ScriptNode(ctx.statement().map(this::visitStatement).flatten())
    }

    private fun visitStatement(ctx: StatementContext): List<StatementNode> {
        val simple = ctx.simpleStatement()
        return if (simple != null) {
            visitSimpleStatement(simple)
        } else {
            listOf(visitCompoundStatement(ctx.compoundStatement()))
        }
    }

    private fun visitSimpleStatement(ctx: SimpleStatementContext): List<StatementNode> {
        return ctx.smallStatement().map(::visitSmallStatement)
    }

    private fun visitSmallStatement(ctx: SmallStatementContext): StatementNode {
        val exprStatement = ctx.exprStatement()
        if (exprStatement != null) {
            return StatementNode(ctx.lineNumber(), visitExprStatement(exprStatement))
        }

        val flowStatement = ctx.flowStatement()
        if (flowStatement != null)
            return visitFlowStatement(flowStatement)

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
                return ReturnNode(ctx.lineNumber(), visitTest(tests[0]))
            }
        }

        val breakStatement = ctx.breakStatement()
        if (breakStatement != null)
            return BreakNode(ctx.lineNumber())

        val continueStatement = ctx.continueStatement()
        if (continueStatement != null)
            return ContinueNode(ctx.lineNumber())

        val raiseStatement = ctx.raiseStatement()
        if (raiseStatement != null) {
            if (raiseStatement.test().isEmpty() || raiseStatement.test().size > 1)
                TODO()

            return RaiseNode(raiseStatement.lineNumber(), visitTest(raiseStatement.test().first()))
        }

        TODO("Handle other branch possibilities")
    }

    private fun visitCompoundStatement(ctx: CompoundStatementContext): StatementNode {
        val functionCtx = ctx.funcDef()
        if (functionCtx != null)
            return visitFuncDef(functionCtx)

        val ifStatement = ctx.ifStatement()
        if (ifStatement != null)
            return visitIfStatement(ifStatement)

        val whileStatement = ctx.whileStatement()
        if (whileStatement != null)
            return visitWhileStatement(whileStatement)

        val forStatement = ctx.forStatement()
        if (forStatement != null)
            return visitForStatement(forStatement)

        TODO()
    }

    private fun visitForStatement(ctx: ForStatementContext): StatementNode {
        val elseBlock = ctx.elseBlock()

        return ForStatementNode(
            ctx.lineNumber(),
            visitExprList(ctx.exprList()),
            visitTestList(ctx.testList()),
            visitSuite(ctx.suite()),
            if (elseBlock == null) emptyList() else visitSuite(elseBlock.suite())
        )
    }

    private fun visitExprList(ctx: ExprListContext): ExpressionNode {
        val elems = ctx.exprListElem()
        return if (elems.size == 1)
            visitExprListElem(elems[0])
        else
            TupleLiteral(ctx.lineNumber(), elems.map(::visitExprListElem))
    }

    private fun visitExprListElem(ctx: ExprListElemContext): ExpressionNode {
        if (ctx.starExpression() != null)
            TODO()

        return visitExpression(ctx.expression())
    }

    private fun visitTestList(ctx: TestListContext): ExpressionNode {
        val tests = ctx.test()
        return if (tests.size == 1)
            visitTest(tests[0])
        else
            TupleLiteral(ctx.lineNumber(), tests.map(::visitTest))
    }

    private fun visitWhileStatement(ctx: WhileStatementContext): StatementNode {
        val test = ctx.test()
        val body = ctx.suite()
        val elseBlock = ctx.elseBlock()

        return WhileStatementNode(
            ctx.lineNumber(),
            visitTest(test),
            visitSuite(body),
            elseBlock?.let { visitSuite(it.suite()) } ?: emptyList()
        )
    }

    private fun visitIfStatement(ctx: IfStatementContext): StatementNode {
        val ifBlock = ctx.ifBlock()
        val elifBlocks = ctx.elifBlock()
        val elseBlock = ctx.elseBlock()

        return IfStatementNode(
            ctx.lineNumber(),
            IfConditionalNode(
                IfConditionalNodeType.IF,
                visitTest(ifBlock.test()),
                visitSuite(ifBlock.suite())
            ),
            elifBlocks.map {
                IfConditionalNode(
                    IfConditionalNodeType.ELIF,
                    visitTest(it.test()),
                    visitSuite(it.suite())
                )
            },
            elseBlock?.let {
                visitSuite(it.suite())
            } ?: emptyList()
        )
    }

    private fun visitSuite(ctx: SuiteContext): List<StatementNode> {
        val simpleStatement = ctx.simpleStatement()

        if (simpleStatement != null)
            return visitSimpleStatement(simpleStatement)

        return ctx.statement().map(::visitStatement).flatten()
    }

    private fun visitFuncDef(ctx: FuncDefContext): FunctionNode {
        // TODO: Can this ever be null? Not sure if all functions have
        // to be named given that Python provides explicit lambda support
        val name = ctx.NAME().text
        val typedArgsList = ctx.typedArgsList()
        val test = ctx.test() // TODO: What is this?

        val suite = ctx.suite()
        val parameters = if (typedArgsList != null) visitTypedArgsList(typedArgsList) else emptyList()

        if (test != null) {
            TODO("Handle other possibilities")
        }

        val statements = mutableListOf<StatementNode>()

        val simpleStatement = suite.simpleStatement()

        if (simpleStatement != null) {
            statements.addAll(visitSimpleStatement(simpleStatement))
        } else {
            statements.addAll(suite.statement().map(::visitStatement).flatten())
        }

        return FunctionNode(ctx.lineNumber(), IdentifierNode(ctx.lineNumber(), name), parameters, statements)
    }

    private fun visitTypedArgsList(ctx: TypedArgsListContext): List<ParameterNode> {
        if (ctx.normalArgs == null)
            TODO()

        return visitTypedArgsListNoPosOnly(ctx.normalArgs)
    }

    private fun visitTypedArgsListNoPosOnly(ctx: TypedArgsListNoPosOnlyContext): List<ParameterNode> {
        if (ctx.argsKwonlyKwargs() != null)
            TODO()

        return visitPosKeywordArgsKwonlyKwargs(ctx.posKeywordArgsKwonlyKwargs())
    }

    private fun visitPosKeywordArgsKwonlyKwargs(ctx: PosKeywordArgsKwonlyKwargsContext): List<ParameterNode> {
        if (ctx.argsKwonlyKwargs() != null)
            TODO()

        return ctx.parameters().parameter().map(::visitParameter)
    }

    private fun visitParameter(ctx: ParameterContext): ParameterNode {
        val ident = IdentifierNode(ctx.lineNumber(), ctx.tfpDef().NAME().text)

        return ParameterNode(
            ident,
            if (ctx.test() != null) visitTest(ctx.test()) else null
        )
    }

    private fun visitExprStatement(ctx: ExprStatementContext): ExpressionNode {
        val testListStarExpr = ctx.testlistStarExpression()

        if (ctx.augAssignment() != null || ctx.annAssign() != null)
            TODO()

        val annAssignment = ctx.annAssignment()
        if (annAssignment.isNotEmpty()) {
            if (annAssignment.size > 1)
                TODO()

            val testListExprs = visitTestListStarExpr(testListStarExpr)

            if (testListExprs !is IdentifierNode)
                TODO()

            return AssignmentNode(
                ctx.lineNumber(),
                testListExprs,
                visitAnnAssignment(annAssignment[0])
            )
        }

        return visitTestListStarExpr(testListStarExpr)
    }

    private fun visitAnnAssignment(ctx: AnnAssignmentContext): ExpressionNode {
        if (ctx.yieldExpression().isNotEmpty())
            TODO()

        val testListStarExprs = ctx.testlistStarExpression()
        if (testListStarExprs.isEmpty() || testListStarExprs.size > 2)
            TODO()

        return visitTestListStarExpr(testListStarExprs[0])
    }

    private fun visitTestListStarExpr(ctx: TestlistStarExpressionContext): ExpressionNode {
        val testListElems = ctx.testlistElem()

        return if (testListElems.size == 1) {
            visitTestlistElem(testListElems[0])
        } else {
            // TODO: Is this always a tuple?
            TupleLiteral(ctx.lineNumber(), testListElems.map(::visitTestlistElem))
        }
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
            andTests.reduce { prev, curr ->
                OrExpresionNode(ctx.lineNumber(), prev, curr)
            }
        }
    }

    private fun visitAndTest(ctx: AndTestContext): ExpressionNode {
        val notTests = ctx.notTest().map(::visitNotTest)

        return if (notTests.size == 1) {
            notTests[0]
        } else {
            notTests.reduce { prev, curr ->
                AndExpressionNode(ctx.lineNumber(), prev, curr)
            }
        }
    }

    private fun visitNotTest(ctx: NotTestContext): ExpressionNode {
        val notTest = ctx.notTest()

        return if (notTest != null) {
            NotExpressionNode(ctx.lineNumber(), visitNotTest(notTest))
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
                ctx.lineNumber(),
                ComparisonOperator.from(operators[0].text),
                visitExpression(expressions[0]),
                visitExpression(expressions[1])
            )

            operators.drop(1).forEachIndexed { index, compCtx ->
                compNode = ComparisonNode(
                    compCtx.lineNumber(),
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
                    ctx.lineNumber(),
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
                    ctx.lineNumber(),
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
                    ctx.lineNumber(),
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
                ctx.lineNumber(),
                ArithmeticOperator.from(operators[0].text),
                visitArithmeticExpr(arithmeticExprs[0]),
                visitArithmeticExpr(arithmeticExprs[1])
            )

            operators.drop(1).forEachIndexed { index, bitShiftCtx ->
                bitShiftNode = ArithmeticExpressionNode(
                    bitShiftCtx.lineNumber(),
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
                ctx.lineNumber(),
                ArithmeticOperator.from(operators[0].text),
                visitTerm(terms[0]),
                visitTerm(terms[1])
            )

            operators.drop(1).forEachIndexed { index, arithCtx ->
                arithNode = ArithmeticExpressionNode(
                    arithCtx.lineNumber(),
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
                ctx.lineNumber(),
                ArithmeticOperator.from(operators[0].text),
                visitFactor(factors[0]),
                visitFactor(factors[1])
            )

            operators.drop(1).forEachIndexed { index, termCtx ->
                arithNode = ArithmeticExpressionNode(
                    termCtx.lineNumber(),
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
            ctx.lineNumber(),
            UnaryOperator.from(op.text),
            visitFactor(factor)
        )
    }

    private fun visitPower(ctx: PowerContext): ExpressionNode {
        val atomExpr = ctx.atomExpression()
        val factor = ctx.factor()

        return if (factor != null) {
            ArithmeticExpressionNode(
                ctx.lineNumber(),
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
            makeTrailers(ctx.lineNumber(), atomNode, trailers)
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
        if (ctx.yieldExpression() != null)
            TODO()

        val testListCtx = ctx.testListComp()
        val numTests = testListCtx.testlistElem().size
        val numCommas = testListCtx.COMMA().size

        return if (numTests == 0 && numCommas == 0) {
            TupleLiteral(ctx.lineNumber(), emptyList())
        } else if (numTests == 1 && numCommas == 0) {
            visitTestListComp(testListCtx)[0]
        } else {
            // numCommas > 0, which implies numTests > 1
            TupleLiteral(ctx.lineNumber(), visitTestListComp(testListCtx))
        }
    }

    private fun visitListAtom(ctx: ListAtomContext): ExpressionNode {
        return ListLiteral(ctx.lineNumber(), ctx.testListComp()?.let(::visitTestListComp) ?: emptyList())
    }

    private fun visitTestListComp(ctx: TestListCompContext): List<ExpressionNode> {
        if (ctx.compFor() != null)
            TODO()
        return ctx.testlistElem().map(::visitTestlistElem)
    }

    private fun visitTestlistElem(ctx: TestlistElemContext): ExpressionNode {
        if (ctx.starExpression() != null)
            TODO()
        return visitTest(ctx.test())
    }

    private fun visitDictOrSetAtom(ctx: DictOrSetAtomContext): ExpressionNode {
        val dictMaker = ctx.dictMaker()
        if (dictMaker != null)
            return visitDictMaker(dictMaker)

        TODO()
    }

    private fun visitDictMaker(ctx: DictMakerContext): ExpressionNode {
        return DictLiteral(
            ctx.lineNumber(),
            ctx.dictTerm().map {
                if (it.compFor() != null || it.kstarExpression() != null)
                    TODO()

                visitTest(it.id) to visitTest(it.value)
            }.toMap()
        )
    }

    private fun visitBasicAtom(ctx: BasicAtomContext): ExpressionNode {
        return ctx.NAME()?.let { IdentifierNode(ctx.lineNumber(), it.text) } ?:
                ctx.number()?.let { visitNumber(it) } ?:
                ctx.ELLIPSIS()?.let { EllipsisNode(ctx.lineNumber()) } ?:
                ctx.NONE()?.let { NoneNode(ctx.lineNumber()) } ?:
                ctx.TRUE()?.let { TrueNode(ctx.lineNumber()) } ?:
                ctx.FALSE()?.let { FalseNode(ctx.lineNumber()) } ?:
                makeStrings(ctx.lineNumber(), ctx.STRING())
    }

    private fun visitNumber(ctx: NumberContext): ExpressionNode {
        val intCtx = ctx.integer()
        if (intCtx != null) {
            var token = intCtx.dec
            if (token != null)
                return IntegerLiteral(ctx.lineNumber(), Integer.parseInt(token.text, 10))

            token = intCtx.oct
            if (token != null)
                return IntegerLiteral(ctx.lineNumber(), Integer.parseInt(token.text.replace("0o", "").replace("0O", ""), 8))

            token = intCtx.hex
            if (token != null)
                return IntegerLiteral(ctx.lineNumber(), Integer.parseInt(token.text.replace("0x", "").replace("0X", ""), 16))

            token = intCtx.bin
            return IntegerLiteral(ctx.lineNumber(), Integer.parseInt(token.text.replace("0b", "").replace("0B", ""), 2))
        }

        val floatTerm = ctx.FLOAT_NUMBER()
        if (floatTerm != null)
            return FloatLiteral(ctx.lineNumber(), java.lang.Double.parseDouble(floatTerm.text))

        val imagTerm = ctx.IMAG_NUMBER()
        return ComplexLiteral(ctx.lineNumber(), java.lang.Double.parseDouble(imagTerm.text.replace("j", "").replace("J", "")))
    }

    private fun makeTrailers(lineNumber: Int, atomNode: ExpressionNode, trailers: List<TrailerContext>): ExpressionNode {
        var node = atomNode

        for (trailer in trailers) {
            val call = trailer.trailerCall()

            if (call != null) {
                node = FunctionCallNode(lineNumber, node, visitArgList(call.argList()))
                continue
            }

            val memberAccess = trailer.trailerMemberAccess()
            if (memberAccess != null) {
                node = MemberAccessNode(lineNumber, node, visitSubscriptList(memberAccess.subscriptList()))
                continue
            }

            val dotAccess = trailer.trailerDotAccess()
            node = DotAccessNode(lineNumber, node, IdentifierNode(lineNumber, dotAccess.NAME().text))
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

    private fun makeStrings(lineNumber: Int, strings: List<TerminalNode>): ExpressionNode {
        return StringLiteral(lineNumber, strings.joinToString(separator = "") {
            it.text.replace("\"", "").replace("'", "")
        })
    }

    private fun codeBug(): Nothing {
        throw IllegalStateException()
    }
}
