package ru.hse.spb

interface Statement : Node

class FunctionDefinition(val name: String, val pars: List<String>, val body: Block) : Statement {
    override fun evaluate(scope: Scope) {
        scope.add(name, this)
    }
}

class Variable(val name: String, val exp: Expression) : Statement {
    override fun evaluate(scope: Scope) {
        scope.add(name, exp.evaluate(scope))
    }
}

class WhileLoop(val exp: Expression, val body: Block) : Statement {
    override fun evaluate(scope: Scope) {
        while (exp.evaluate(scope) != 0) {
            val newScope = Scope(scope)
            body.evaluate(newScope)
            if (scope.containsKey(Node.RETURN_VALUE_IDENTIFIER) && scope.get(Node.RETURN_VALUE_IDENTIFIER) != Unit) {
                break
            }
        }
    }
}

class Conditional(val exp: Expression, val ifTrue: Block, val ifFalse: Block?) : Statement {

    constructor(exp: Expression, ifTrue: Block) : this(exp, ifTrue, null)

    override fun evaluate(scope: Scope) {
        val newScope = Scope(scope)
        if (exp.evaluate(scope) != 0) {
            ifTrue.evaluate(newScope)
        } else {
            ifFalse?.evaluate(newScope)
        }
    }
}

class Assignment(val name: String, val exp: Expression) : Statement {
    override fun evaluate(scope: Scope) {
        scope.put(name, exp.evaluate(scope))
    }
}

class PrintStatement(val args: List<Expression>) : Statement {
    override fun evaluate(scope: Scope) {
        for (exp in args) {
            print(exp.evaluate(scope))
            print(" ")
        }
        println()
    }

}

class ReturnStatement(val exp: Expression) : Statement {
    override fun evaluate(scope: Scope) {
        scope.put(Node.RETURN_VALUE_IDENTIFIER, exp.evaluate(scope))
    }
}

interface Expression : Statement