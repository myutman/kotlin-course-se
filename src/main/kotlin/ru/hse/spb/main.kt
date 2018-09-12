package ru.hse.spb

import java.util.*

enum class Type { OPEN, ADD_QUERY, REM_QUERY, CLOSE }

data class Event(val x: Int, val y: Int, val type: Type) : Comparable<Event> {
    override fun compareTo(other: Event): Int {
        return if (x != other.x) x.compareTo(other.x) else type.compareTo(other.type)
    }
}

data class Seg(val x1: Int, val x2: Int, val y: Int) : Comparable<Seg> {
    override fun compareTo(other: Seg): Int {
        return x1.compareTo(other.x1)
    }

    val len
        get() = x2 - x1 + 1
}

fun unique(source: ArrayList<Seg>): ArrayList<Seg> {
    source.sort()
    val left = HashMap<Int, Int>()
    val right = HashMap<Int, Int>()
    val ans = ArrayList<Seg>()
    for (seg in source) {
        if (right.containsKey(seg.y) && right.get(seg.y) !!>= seg.x1) {
            if (right.get(seg.y) !!<= seg.x2) {
                right.put(seg.y, seg.x2)
            }
        } else {
            if (right.containsKey(seg.y)) {
                ans.add(Seg(left.get(seg.y)!!, right.get(seg.y)!!, seg.y))
            }
            left.put(seg.y, seg.x1)
            right.put(seg.y, seg.x2)
        }
    }
    for (y in left.keys) {
        ans.add(Seg(left.get(y)!!, right.get(y)!!, y))
    }
    return ans
}

var array: IntArray = intArrayOf()

fun add(x: Int, value: Int) {
    var x1 = x
    while (x1 < array.size) {
        array[x1] += value
        x1 = x1 or (x1 + 1)
    }
}

fun get(x: Int): Int {
    var x1 = x
    var ans = 0
    while (x1 >= 0) {
        ans += array[x1]
        x1 = (x1 and (x1 + 1)) - 1
    }
    return ans
}

fun <T: Comparable<T>> ArrayList<T>.lowerBound(key: T): Int {
    var l = 0
    var r = size
    while (l < r - 1) {
        val m = (l + r) / 2
        if (get(m) == key) return m
        else if (get(m) > key) r = m
        else l = m + 1
    }
    return l
}

fun main(args: Array<String>) {
    val n = readLine()!!.toInt()
    var vertical = ArrayList<Seg>()
    var horisontal = ArrayList<Seg>()
    for (i in 1..n) {
        var (x1, y1, x2, y2) = readLine()!!.split(' ').map { Integer.parseInt(it) }
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

    array = IntArray(ys.size)

    for (event in events) {
        val y = ys.lowerBound(event.y)
        when (event.type) {
            Type.OPEN -> add(y, 1)
            Type.CLOSE -> add(y, -1)
            Type.REM_QUERY -> ans += get(y - 1)
            Type.ADD_QUERY -> ans -= get(y)
        }
    }

    print(ans)
}