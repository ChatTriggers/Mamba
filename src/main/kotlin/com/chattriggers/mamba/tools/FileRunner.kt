package com.chattriggers.mamba.tools

import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Parser
import com.chattriggers.mamba.ast.ASTTransformer
import java.io.File

const val FILE_PATH = "./scripts/index.py"

fun main() {
    val mainFile = File(FILE_PATH)

    val tree = Parser.parseFromFile(mainFile)
    val script = ASTTransformer.transform(tree)
    script.print(0)

    Interpreter.execute(script)
}