package ru.hse.spb

import javax.print.attribute.standard.MediaSize

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text\n")
    }
}

@DslMarker
annotation class TexCommandMarker

@TexCommandMarker
abstract class TexCommand(val name: String, val options: List<String> = emptyList()) : Element {
    val children = arrayListOf<Element>()

    protected fun <T : Element> initCommand(command: T, init: T.() -> Unit): T {
        command.init()
        children.add(command)
        return command
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\$name${renderAttributes()}")
    }

    protected fun renderAttributes(): String {
        if (options.isEmpty()) {
            return "";
        }
        val builder = StringBuilder()
        builder.append("[")
        var flag = false
        for (attr in options) {
            if (flag) {
                builder.append(", ")
            } else {
                flag = true;
            }
            builder.append(attr)
        }
        builder.append("]")
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class TexCommandWithBody(name: String, options: List<String> = emptyList()) : TexCommand(name, options) {
    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\begin{$name}${renderAttributes()}\n")
        for (c in children) {
            c.render(builder, indent + "  ")
        }
        builder.append("$indent\\end{$name}\n")
    }
}

class Alignment(options: List<String> = emptyList()) : TexCommandWithBody("align", options)
class Math(options: List<String> = emptyList()) : TexCommandWithBody("math", options)
class CustomTag(name:String, options: List<String> = emptyList()) : TexCommandWithBody(name, options)

abstract class NonTerminalTexCommandWithBody(name: String, options: List<String> = emptyList())
    : TexCommandWithBody(name, options) {

    fun alignment(vararg options: String, init: Alignment.() -> Unit) = initCommand(Alignment(options.asList()), init)
    fun math(vararg options: String, init: Math.() -> Unit) = initCommand(Math(options.asList()), init)
    fun itemize(vararg options: String, init: Itemize.() -> Unit) = initCommand(Itemize(options.asList()), init)
    fun enumerate(vararg options: String, init: Enumerate.() -> Unit) = initCommand(Enumerate(options.asList()), init)
    fun customTag(name:String, vararg options: String, init: CustomTag.() -> Unit) = initCommand(CustomTag(name, options.asList()), init)
}

class Item : NonTerminalTexCommandWithBody("item") {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent\\item\n")
        for (c in children) {
            c.render(builder, indent + "  ")
        }
    }
}

abstract class ContainingItems(name: String, options: List<String> = emptyList()) : TexCommandWithBody(name, options) {
    fun item(init: Item.() -> Unit) = initCommand(Item(), init)
}

class Itemize(options: List<String> = emptyList()) : ContainingItems("itemize", options)
class Enumerate(options: List<String> = emptyList()) : ContainingItems("enumerate", options)

abstract class TexCommandWithoutBody(name: String, options: List<String> = emptyList(), val text: String) : TexCommand(name, options){
    override fun render(builder: StringBuilder, indent: String) {
        super.render(builder, indent)
        builder.append("{$text}\n")
    }
}

class DocumentClass(className: String, options: List<String>) : TexCommandWithoutBody("documentclass", options, className)
class UsePackage(packageName: String, options: List<String>) : TexCommandWithoutBody("usepackage", options, packageName)
class FrameTitle(frameTitle: String): TexCommandWithoutBody("frametitle", text = frameTitle)

class Frame(options: List<String>, frameTitle: String) : NonTerminalTexCommandWithBody("frame", options) {
    init {
        children.add(FrameTitle(frameTitle))
    }
}

class Document : NonTerminalTexCommandWithBody("document") {

    private var documentClass: DocumentClass? = null
    private val usePackages = ArrayList<UsePackage>()

    fun documentClass(name: String, vararg options: String){
        documentClass = DocumentClass(name, options.asList())
    }

    fun usepackage(packageName: String, vararg options: String) {
        usePackages.add(UsePackage(packageName, options.asList()))
    }

    override fun render(builder: StringBuilder, indent: String) {
        documentClass?.render(builder, indent)
        for (usePackage in usePackages) {
            usePackage.render(builder, indent)
        }
        super.render(builder, indent)
    }

    fun frame(frameTitle: String, vararg options: String, init: Frame.() -> Unit) = initCommand(Frame(options.asList(), frameTitle), init)
}


fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}

infix fun String.to(other: String): String {
    return "$this=$other"
}

fun main(args: Array<String>) {
    val rows = listOf<String>("hello", "is", "there", "anybody", "in", "there")
    print(document {
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
    }.toString())
}