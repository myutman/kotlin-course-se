package ru.hse.spb

import java.util.*
import kotlin.collections.ArrayList

private enum class Type { OPEN, ADD_QUERY, REM_QUERY, CLOSE }

private data class Event(val x: Int, val y: Int, val type: Type) : Comparable<Event> {
    override fun compareTo(other: Event): Int {
        return if (x != other.x) x.compareTo(other.x) else type.compareTo(other.type)
    }
}

private data class Seg(val x1: Int, val x2: Int, val y: Int) : Comparable<Seg> {
    override fun compareTo(other: Seg): Int {
        return x1.compareTo(other.x1)
    }

    val len
        get() = x2 - x1 + 1
}

private fun unique(src: List<Seg>): List<Seg> {
    val source = ArrayList<Seg>(src)
    source.sort()
    val left = HashMap<Int, Int>()
    val right = HashMap<Int, Int>()
    val ans = ArrayList<Seg>()
    for (seg in source) {
        if (right.containsKey(seg.y) && right[seg.y]!! >= seg.x1) {
            if (right[seg.y]!! <= seg.x2) {
                right[seg.y] = seg.x2
            }
        } else {
            if (right.containsKey(seg.y)) {
                ans.add(Seg(left[seg.y]!!, right[seg.y]!!, seg.y))
            }
            left[seg.y] = seg.x1
            right[seg.y] = seg.x2
        }
    }
    for (y in left.keys) {
        ans.add(Seg(left[y]!!, right[y]!!, y))
    }
    return ans
}

private fun IntArray.add(x: Int, value: Int) {
    var x1 = x
    while (x1 < size) {
        this[x1] += value
        x1 = x1 or (x1 + 1)
    }
}

private fun IntArray.gt(x: Int): Int {
    var x1 = x
    var ans = 0
    while (x1 >= 0) {
        ans += this[x1]
        x1 = (x1 and (x1 + 1)) - 1
    }
    return ans
}

private fun <T: Comparable<T>> List<T>.lowerBound(key: T): Int {
    var l = 0
    var r = size
    while (l < r - 1) {
        val m = (l + r) / 2
        when {
            this[m] == key -> return m
            this[m] > key -> r = m
            else -> l = m + 1
        }
    }
    return l
}

private fun initSegs(): Pair<List<Seg>, List<Seg>> {
    val n = readLine()!!.toInt()
    val vertical = ArrayList<Seg>()
    val horisontal = ArrayList<Seg>()
    for (i in 1..n) {
        var (x1, y1, x2, y2) = readLine()!!.split(' ').map { it.toInt() }
        if (x1 == x2) {
            if (y1 > y2) {
                val t = y1
                y1 = y2
                y2 = t
            }
            vertical.add(Seg(y1, y2, x1))
        } else {
            if (x1 > x2) {
                val t = x1
                x1 = x2
                x2 = t
            }
            horisontal.add(Seg(x1, x2, y1))
        }
    }
    return Pair(vertical, horisontal)
}

fun main(args: Array<String>) {

    var (vertical: List<Seg>, horisontal: List<Seg>) = initSegs()
    vertical = unique(vertical)
    horisontal = unique(horisontal)

    val events = ArrayList<Event>()
    val ys = ArrayList<Int>()

    var ans: Long = 0

    for (seg in vertical) {
        events.add(Event(seg.y, seg.x1, Type.REM_QUERY))
        events.add(Event(seg.y, seg.x2, Type.ADD_QUERY))
        ys.add(seg.x1)
        ys.add(seg.x2)
        ans += seg.len
    }

    for (seg in horisontal) {
        events.add(Event(seg.x1, seg.y, Type.OPEN))
        events.add(Event(seg.x2, seg.y, Type.CLOSE))
        ys.add(seg.y)
        ans += seg.len
    }

    ys.sort()
    events.sort()

    val ar = IntArray(ys.size)

    for (event in events) {
        val y = ys.lowerBound(event.y)
        when (event.type) {
            Type.OPEN -> ar.add(y, 1)
            Type.CLOSE -> ar.add(y, -1)
            Type.REM_QUERY -> ans += ar.gt(y - 1)
            Type.ADD_QUERY -> ans -= ar.gt(y)
        }
    }

    print(ans)
}