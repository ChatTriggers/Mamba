package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.exceptions.VBaseException

abstract class Node(val lineNumber: Int, val children: List<Node>) {
    internal var parent: Node? = null

    init {
        children.forEach {
            it.parent = this
        }
    }

    abstract fun execute(ctx: ThreadContext): VObject

    /* fun compile(compiler: Compiler) */

    abstract fun print(indent: Int)

    internal inline fun <reified T> getParentOfType(): T? {
        var p = parent

        while (p != null) {
            if (p is T)
                return p
            p = p.parent
        }

        return null
    }

    inline fun VObject.ifException(block: (VBaseException) -> Unit): VObject {
        if (this is VBaseException) {
            initializeCallstack(lineNumber)
            block(this)
        }

        return this
    }

    companion object {
        protected const val INDENT_MULTIPLIER = 2

        fun printIndent(indent: Int) {
            repeat(INDENT_MULTIPLIER * indent) {
                print(' ')
            }
        }

        fun printNodeHeader(indent: Int, node: Node, newLine: Boolean = true) {
            printIndent(indent)
            print(node::class.java.simpleName)

            if (newLine)
                println()
        }
    }
}