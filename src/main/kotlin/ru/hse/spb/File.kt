package ru.hse.spb

class File(val block: Block)  : Node {
    override fun evaluate() {
        block.evaluate()
    }
}