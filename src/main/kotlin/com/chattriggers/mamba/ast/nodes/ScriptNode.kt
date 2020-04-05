package com.chattriggers.mamba.ast.nodes

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.values.singletons.VNone
import com.chattriggers.mamba.core.values.VObject
import com.chattriggers.mamba.ast.nodes.statements.StatementNode
import com.chattriggers.mamba.core.CallFrame
import com.chattriggers.mamba.core.values.exceptions.MambaException

class ScriptNode(private val statements: List<StatementNode>) : Node(1, statements) {
    override fun execute(interp: Interpreter): VObject {
        try {
            statements.forEach {
                it.execute(interp)
            }
        } catch (e: MambaException) {
            // TODO: This entire process is extremely hacky
            interp.callStack.push(CallFrame(interp.fileName, "<module>", 1))

            println("Traceback (most recent call last):")

            val reversed = interp.callStack.reversed()

            reversed.forEachIndexed { index, stack ->
                if (index == 0) return@forEachIndexed

                println(
                    "  File \"${stack.file}\", line ${stack.lineNumber}, in ${reversed[index - 1].source}\n" +
                    "    ${interp.lines[stack.lineNumber - 1].trim()}"
                )
            }

            println(e.reason.toString())
        }

        return VNone
    }

    override fun print(indent: Int) {
        printNodeHeader(indent, this)
        statements.forEach { it.print(indent + 1) }
    }
}