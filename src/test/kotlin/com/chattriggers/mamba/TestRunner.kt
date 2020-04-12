package com.chattriggers.mamba

import com.chattriggers.mamba.ast.ASTTransformer
import com.chattriggers.mamba.core.Interpreter
import com.chattriggers.mamba.core.Parser
import com.chattriggers.mamba.core.ThreadContext
import com.chattriggers.mamba.core.values.base.VObject
import com.chattriggers.mamba.core.values.collections.VString
import com.chattriggers.mamba.core.values.exceptions.VAssertionError
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

    /*
     * The Python assert statement is used to run our test
     * suite, so in the following tests we test the assert
     * statement to ensure the remaining tests will function
     * correctly
     */

    @Test
    fun testAssertStatementPassesWithTrue() {
        val script = "assert True"

        val result = testAssertStatement(script)
        assert(result !is VBaseException) {
            "\n" + (result as VBaseException)
        }
    }

    @Test
    fun testAssertStatementPassesWithTrueAndDesc() {
        val script = "assert True, \"test message\""

        val result = testAssertStatement(script)
        assert(result !is VBaseException) {
            "\n" + (result as VBaseException)
        }
    }

    @Test
    fun testAssertStatementThrowsWithFalse() {
        val script = "assert False"

        val result = testAssertStatement(script)
        assert(result is VAssertionError) {
            "\n" + result
        }
    }

    @Test
    fun testAssertStatementThrowsWithFalseAndDesc() {
        val script = "assert False, \"test message\""

        val result = testAssertStatement(script)
        assert(result is VAssertionError) {
            "Expected AssertError, got ${result.className}"
        }

        val err = result as VAssertionError
        assert(err.args.items.size == 1) {
            "Expected VAssertError.args tuple to have a size of 1, instead got size of ${err.args.items.size}"
        }

        val item = err.args.items[0]
        assert(item is VString) {
            "Expected VAssertError.args.items[0] to be of type VString, instead got ${item.className}"
        }
        assert((item as VString).string == "test message") {
            "Expected VAssertError.args.items[0].string to be \"test message\", instead got ${item.string}"
        }
    }

    private fun testAssertStatement(script: String): VObject {
        val tree = Parser.parseFromString(script)
        val ast = ASTTransformer.transform(tree)
        val interpreter = Interpreter("", listOf(script))
        val context = ThreadContext(interpreter)
        ThreadContext.enterContext(context)

        try {
            return interpreter.execute(ast)
        } finally {
            ThreadContext.exitContext()
        }
    }

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
