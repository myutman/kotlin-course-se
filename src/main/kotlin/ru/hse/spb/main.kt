package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files

fun buildAST(code: String) : Node {
    val expLexer = ExpLexer(CharStreams.fromString(code))
    val expParser = ExpParser(BufferedTokenStream(expLexer))
    return expParser.file().value
}

fun execute(code: String) {
    val file = buildAST(code)
    file.evaluate(Scope())
}

fun main(args: Array<String>) {
    val path: String
    if (args.size == 0) {
        path = "src/main/resources/code_sample.txt"
    } else {
        path = args[0]
    }
    execute(File(path).readText(Charsets.UTF_8))
}