package ru.hse.spb

interface Node {
    companion object {
        const val RETURN_VALUE_IDENTIFIER = "return@value"
    }

    fun evaluate(scope: Scope) : Any
}