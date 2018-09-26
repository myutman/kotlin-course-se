package ru.hse.spb

import java.lang.RuntimeException

class FunctionCall(val name: String, val args: List<Expression>) : Expression {
    override fun evaluate(scope: Scope): Int {
        val def: FunctionDefinition = scope.get(name) as FunctionDefinition
        if (args.size != def.pars.size) {
            throw RuntimeException("Wrong number of arguments")
        }
        val newScope = Scope(scope)
        for ((name, exp) in def.pars zip args){
            newScope.add(name, exp.evaluate(scope))
        }
        newScope.add(Node.RETURN_VALUE_IDENTIFIER, Unit)
        def.body.evaluate(newScope)
        val ret = newScope.get(Node.RETURN_VALUE_IDENTIFIER)
        return if (ret == Unit) 0 else ret as Int
    }
}

class BinaryExpression(val left: Expression, val right: Expression, val op: String) : Expression {
    override fun evaluate(scope: Scope): Any {
        val lv: Int = left.evaluate(scope) as Int
        val rv: Int = right.evaluate(scope) as Int
        when (op) {
            "+" -> return lv + rv
            "*" -> return lv * rv
            "-" -> return lv - rv
            "/" -> return lv / rv
            "%" -> return lv % rv
            "==" -> return if (lv == rv) 1 else 0
            "!=" -> return if (lv != rv) 1 else 0
            ">=" -> return if (lv >= rv) 1 else 0
            ">" -> return if (lv > rv) 1 else 0
            "<=" -> return if (lv <= rv) 1 else 0
            "<" -> return if (lv < rv) 1 else 0
            "&&" -> return if ((lv != 0) && (rv != 0)) 1 else 0
            "||" -> return if ((lv != 0) || (rv != 0)) 1 else 0
        }
        throw RuntimeException("Invalid operation")
    }
}

class IdentifierExpression(val name: String) : Expression {
    override fun evaluate(scope: Scope): Any {
        return scope.get(name)
    }
}

class LiteralExpression(val value: Int) : Expression {
    override fun evaluate(scope: Scope): Any {
        return value
    }
}