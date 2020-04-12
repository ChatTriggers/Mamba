package com.chattriggers.mamba

import com.chattriggers.mamba.ast.ASTTransformer
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Parser
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.exceptions.VBaseException
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.streams.toList

class TestRunner {
    private val suite = File("./src/test/kotlin/com/chattriggers/mamba/suite")

    @TestFactory
    fun runPythonTests(): Collection<DynamicTest> {
        val stream = Files.walk(suite.toPath(), Integer.MAX_VALUE)
        return stream.map { it.toFile() }.filter { it.isFile }.toList().map {
            DynamicTest.dynamicTest(it.name) {
                val tree = Parser.parseFromFile(it)
                val script = ASTTransformer.transform(tree)

                val interpreter = Interpreter(it.name, it.readLines())
                val context = ThreadContext(interpreter)
                ThreadContext.enterContext(context)

                try {
                    val output = interpreter.execute(script)

                    assert(output !is VBaseException) {
                        "\n" + (output as VBaseException).toString()
                    }
                } finally {
                    ThreadContext.exitContext()
                }
            }
        }
    }
}
