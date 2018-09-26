package ru.hse.spb

class Block : Node {

    val statements = ArrayList<Statement>()

    fun add(statement: Statement) {
        statements.add(statement)
    }

    override fun evaluate(scope: Scope) {
        for (statement in statements) {
            statement.evaluate(scope)
            if (scope.containsKey(Node.RETURN_VALUE_IDENTIFIER) && scope.get(Node.RETURN_VALUE_IDENTIFIER) != Unit) {
                break
            }
        }
    }
}