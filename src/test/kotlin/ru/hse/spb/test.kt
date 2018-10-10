package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {

    @Test
    fun testDocument() {
        val actual = document { + "1" }.toString()
        assertEquals("""
            |\begin{document}
            |  1
            |\end{document}
            |
        """.trimMargin(), actual)
    }

    @Test
    fun testUsepackageDocumentClass() {
        val actual = document {
            documentClass("beamer", "arg1" to "arg2")
            usepackage("babel", "russian", "english")
        }.toString()
        assertEquals("""
            |\documentclass[arg1=arg2]{beamer}
            |\usepackage[russian, english]{babel}
            |\begin{document}
            |\end{document}
            |
        """.trimMargin(), actual)
    }

    @Test
    fun testFrame() {
        val actual = document {
            frame("title") {
                + "1"
            }
        }.toString()
        assertEquals("""
            |\begin{document}
            |  \begin{frame}
            |    \frametitle{title}
            |    1
            |  \end{frame}
            |\end{document}
            |
        """.trimMargin(), actual)
    }

    @Test
    fun testContainingItems() {
        val actual = document {
            enumerate {
                for (i in 1..3) {
                    item {
                        itemize {
                            for (j in 1..3) {
                                item {
                                    + "$i.$j"
                                }
                            }
                        }
                    }
                }
            }
        }.toString()
        assertEquals("""
            |\begin{document}
            |  \begin{enumerate}
            |    \item
            |      \begin{itemize}
            |        \item
            |          1.1
            |        \item
            |          1.2
            |        \item
            |          1.3
            |      \end{itemize}
            |    \item
            |      \begin{itemize}
            |        \item
            |          2.1
            |        \item
            |          2.2
            |        \item
            |          2.3
            |      \end{itemize}
            |    \item
            |      \begin{itemize}
            |        \item
            |          3.1
            |        \item
            |          3.2
            |        \item
            |          3.3
            |      \end{itemize}
            |  \end{enumerate}
            |\end{document}
            |
        """.trimMargin(), actual)
    }

    @Test
    fun testRest() {
        val actual = document {
            documentClass("beamer")
            usepackage("babel", "russian" /* varargs */)
            frame("frametitle", "arg1" to "arg2") {
                math {
                    + "\\log"
                }

                alignment {
                    + "\\sqrt"
                }

                // begin{pyglist}[language=kotlin]...\end{pyglist}
                customTag("pyglist", "language" to "kotlin") {
                    +"""
               |val a = 1
               |
            """.trimMargin()
                }
            }
        }.toString()
        assertEquals("""
            |\documentclass{beamer}
            |\usepackage[russian]{babel}
            |\begin{document}
            |  \begin{frame}[arg1=arg2]
            |    \frametitle{frametitle}
            |    \begin{math}
            |      \log
            |    \end{math}
            |    \begin{align}
            |      \sqrt
            |    \end{align}
            |    \begin{pyglist}[language=kotlin]
            |      val a = 1
            |
            |    \end{pyglist}
            |  \end{frame}
            |\end{document}
            |
        """.trimMargin(), actual)
    }
}