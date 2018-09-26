package ru.hse.spb

class FunctionCall(val name: String, val args: List<Expression>) : Expression {
    override fun evaluate() {

    }
}

class BinaryExpression(val left: Expression, val right: Expression, val op: String) : Expression {
    override fun evaluate() {

    }
}

class IdentifierExpression(val name: String) : Expression {
    override fun evaluate() {

    }
}

class LiteralExpression(val value: Int) : Expression {
    override fun evaluate() {

    }
}