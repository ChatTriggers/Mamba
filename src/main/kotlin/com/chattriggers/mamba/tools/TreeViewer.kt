package com.chattriggers.mamba.tools

import com.chattriggers.mamba.core.Parser
import java.io.File

fun main() {
    val mainFile = File(FILE_PATH)
    Parser.parseFromFile(mainFile, displayTree = true)
}