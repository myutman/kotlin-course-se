package ru.hse.spb

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestSources {

    private val output = ByteArrayOutputStream()
    private val errors = ByteArrayOutputStream()

    @Before
    fun before() {
        System.setOut(PrintStream(output))
        System.setErr(PrintStream(errors))
    }

    @After
    fun after() {
        System.setOut(System.out)
        System.setErr(System.err)
    }

    @Test
    fun testParse() {
        val file = buildAST("""var a = 10
            var b = 20
            if (a > b) {
                println(1)
            } else {
                println(0)
            }""") as Block
        assertEquals(3, file.statements.size)
        assertNotNull((file.statements[2] as Conditional).ifFalse)
    }

    @Test
    fun testEval() {
        val file = Block()
        val funBody = Block()
        val ifBody = Block()
        ifBody.add(ReturnStatement(LiteralExpression(1)))
        funBody.add(Conditional(BinaryExpression(IdentifierExpression("n"), LiteralExpression(1), "<="),
                ifBody))
        funBody.add(ReturnStatement(BinaryExpression(
                FunctionCall("fib",
                        listOf(BinaryExpression(IdentifierExpression("n"), LiteralExpression(1), "-"))),
                FunctionCall("fib",
                        listOf(BinaryExpression(IdentifierExpression("n"), LiteralExpression(2), "-"))),
                "+")))
        file.add(FunctionDefinition("fib", listOf("n"), funBody))
        file.add(Variable("i", LiteralExpression(1)))
        val whileBody = Block()
        whileBody.add(PrintStatement(listOf(IdentifierExpression("i"),
                FunctionCall("fib", listOf(IdentifierExpression("i"))))))
        whileBody.add(Assignment("i", BinaryExpression(IdentifierExpression("i"), LiteralExpression(1), "+")))
        file.add(WhileLoop(BinaryExpression(IdentifierExpression("i"), LiteralExpression(5), "<="), whileBody))
        file.evaluate(Scope())
        assertEquals("1 1\n" +
                "2 2\n" +
                "3 3\n" +
                "4 5\n" +
                "5 8\n", output.toString())
    }

    @Test
    fun testCombination() {
        execute("""fun foo(n) {
            fun bar(m) {
                return m + n
            }

            return bar(1)
        }

        println(foo(41)) // prints 42""")
        assertEquals("42\n", output.toString())
    }
}