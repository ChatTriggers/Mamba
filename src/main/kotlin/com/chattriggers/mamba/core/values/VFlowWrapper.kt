package com.chattriggers.mamba.core.values

/**
 * This class is a way for child nodes to communicate
 * with their parent about flow statements.
 *
 * For example, consider the following (not necessarily
 * accurate) IR tree:
 *
 *      FunctionNode
 *          IfStatement
 *              ReturnNode
 *              FunctionCallNode
 *                  IdentifierNode "print"
 *                  NumberLiteral "1"
 *
 * It is clear that the FunctionCallNode should never
 * get executed, but in order for that to happen, the
 * IfStatement node has to be able to recognize the
 * value of ReturnNode#execute as an indicator that it
 * should stop looping over it's children.
 */
sealed class VFlowWrapper(val wrapped: VObject) : VObject()

class VReturnWrapper(wrapped: VObject) : VFlowWrapper(wrapped)