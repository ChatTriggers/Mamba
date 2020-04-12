package com.chattriggers.mamba.core

import com.chattriggers.mamba.generated.Python3Lexer
import com.chattriggers.mamba.generated.Python3Parser
import org.antlr.v4.gui.Trees
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.io.File

object Parser {
    internal fun parseFromString(string: String, displayTree: Boolean = false): Python3Parser.FileInputContext {
        val input = CharStreams.fromString(string)
        val parser = createParser(input)
        val parsedStatement = parser.fileInput()

        if (displayTree)
            Trees.inspect(parsedStatement, parser)

        return parsedStatement
    }

    internal fun parseFromFile(file: File, displayTree: Boolean = false): Python3Parser.FileInputContext {
        val input = CharStreams.fromReader(file.reader())
        val parser = createParser(input)
        val parsedFile = parser.fileInput()

        if (displayTree)
            Trees.inspect(parsedFile, parser)

        return parsedFile
    }

    private fun createParser(stream: CharStream): Python3Parser {
        val lexer = Python3Lexer(stream)
        val tokenStream = CommonTokenStream(lexer)

        return Python3Parser(tokenStream)
    }
}