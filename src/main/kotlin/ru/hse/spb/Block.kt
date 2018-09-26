package ru.hse.spb

class Block : Node {

    val statements = ArrayList<Statement>()

    fun add(statement: Statement) {
        statements.add(statement)
    }

    override fun evaluate() {
        for (statement in statements) {
            statement.evaluate()
        }
    }
}