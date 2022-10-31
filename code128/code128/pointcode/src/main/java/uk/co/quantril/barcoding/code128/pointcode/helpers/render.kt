package uk.co.quantril.barcoding.code128.pointcode.helpers

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import kotlin.math.abs
import kotlin.math.sign

internal fun render1(s: Sequence<Int>): String {
    val sb = StringBuilder()
    var first = true
    s.forEach {
        if (!first) sb.append(" ")
        sb.append("("+intToString3(it)+")")
        first = false
    }
    return sb.toString()
}

internal fun render2(s: Sequence<Pair<Int,CodeSet>>): String {
    val sb = StringBuilder()
    var first = true
    s.forEach {
        if (!first) sb.append(" ")
        sb.append("("+intToString3(it.first)+","+it.second.toLetter().toString()+")")
        first = false
    }
    return sb.toString()
}

internal fun intToString3(x: Int): String {
    require(x>=0)
    val s = "000" + x.toString().trim()
    val s2 = s.substring(s.length-3,s.length)
    return s2
}

private fun CodeSet.toLetter(): Char {
    return when(this) {
        CodeSet.Seta -> 'A'
        CodeSet.Setb -> 'B'
        CodeSet.Setc -> 'C'
    }
}