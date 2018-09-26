package ru.hse.spb

import java.lang.RuntimeException

class Scope{
    private val dictionary: MutableMap<String, Any> = HashMap()
    private val parent: Scope?

    constructor() {
        parent = null
    }

    constructor(parent: Scope) {
        this.parent = parent
    }

    fun get(key: String): Any {
        val v = dictionary[key] ?: parent?.get(key) ?: throw RuntimeException("Variable $key not found")
        return v
    }

    fun add(key: String, value: Any) {
        if (dictionary.containsKey(key)) {
            throw RuntimeException("Variable $key was declared before")
        }
        dictionary[key] = value
    }

    fun put(key: String, value: Any) {
        if (!dictionary.containsKey(key)) {
            parent?.put(key, value) ?: throw RuntimeException("Variable $key not found")
        } else {
            dictionary[key] = value
        }
    }

    fun containsKey(key: String): Boolean {
        return if (dictionary.containsKey(key)) true else parent?.containsKey(key) ?: false
    }
}