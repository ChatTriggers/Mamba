package com.chattriggers.mamba.tools

import com.chattriggers.mamba.ast.ASTTransformer
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Parser
import com.chattriggers.mamba.core.ThreadContext
import java.io.File

const val FILE_PATH = "./scripts/index.py"

fun main() {
    val mainFile = File(FILE_PATH)

    val tree = Parser.parseFromFile(mainFile)
    val script = ASTTransformer.transform(tree)

    val interpreter = Interpreter(mainFile.name, mainFile.readLines())
    val context = ThreadContext(interpreter)
    ThreadContext.enterContext(context)

    try {
        val output = interpreter.execute(script)

        println("Program output:")
        println(output)
    } finally {
        ThreadContext.exitContext()
    }
}