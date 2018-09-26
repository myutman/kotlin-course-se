package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

fun getGreeting(): String {
    val words = mutableListOf<String>()
    words.add("Hello,")
    words.add("world!")

    return words.joinToString(separator = " ")
}

fun main(args: Array<String>) {
    val expLexer = ExpLexer(CharStreams.fromString("""var a = 10
            var b = 20
            if (a > b) {
                b = 1
            } else {
                b = 0
            }"""))

    val expParser = ExpParser(BufferedTokenStream(expLexer))
    val file = expParser.file().value
    //println(ExpParser(BufferedTokenStream(expLexer)).parse().value)
}