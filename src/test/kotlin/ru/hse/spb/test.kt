package ru.hse.spb

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class TestSource {

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

    private fun setInput(s: String) {
        System.setIn(ByteArrayInputStream(s.toByteArray()))
    }

    @Test
    fun test1() {
        setInput("3\n" +
                "0 1 2 1\n" +
                "1 4 1 2\n" +
                "0 3 2 3\n")
        main(emptyArray())
        assertEquals("8", output.toString())
    }

    @Test
    fun test2() {
        setInput("4\n" +
                "-2 -1 2 -1\n" +
                "2 1 -2 1\n" +
                "-1 -2 -1 2\n" +
                "1 2 1 -2\n")
        main(emptyArray())
        assertEquals("16", output.toString())
    }

    @Test
    fun test3() {
        setInput("4\n" +
                "0 0 2 0\n" +
                "1 1 1 -1\n" +
                "1 0 1 -2\n" +
                "1 -1 1 -1")
        main(emptyArray())
        assertEquals("6", output.toString())
    }
}