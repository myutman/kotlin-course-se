package ru.hse.spb

interface Statement : Node

class Function(val name: String, val pars: List<String>, val body: Block) : Statement {
    override fun evaluate() {

    }
}

class Variable(val name: String, val exp: Expression) : Statement {
    override fun evaluate() {

    }
}

class WhileLoop(val exp: Expression, val body: Block) : Statement {
    override fun evaluate() {

    }
}

class Conditional(val exp: Expression, val ifTrue: Block, val ifFalse: Block?) : Statement {

    constructor(exp: Expression, ifTrue: Block) : this(exp, ifTrue, null)

    override fun evaluate() {

    }
}

class Assignment(val name: String, val exp: Expression) : Statement {
    override fun evaluate() {

    }
}

class ReturnStatement(val exp: Expression) : Statement {
    override fun evaluate() {

    }
}

interface Expression : Statement