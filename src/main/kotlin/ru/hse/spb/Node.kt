package ru.hse.spb

interface Node {
    companion object {
        val dict = HashMap<String, Any>()
    }
    fun evaluate() : Any
}