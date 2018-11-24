package ru.hse.spb

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.File
import java.io.FileNotFoundException
import java.lang.RuntimeException

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
    val path = if (args.isEmpty())
                "src/main/resources/code_sample.txt"
            else
                args[0]
    try {
        execute(File(path).readText(Charsets.UTF_8))
    } catch (e: FileNotFoundException) {
        System.err.println(e.message)
    } catch (e: RuntimeException) {
        System.err.println(e.message)
    }
}