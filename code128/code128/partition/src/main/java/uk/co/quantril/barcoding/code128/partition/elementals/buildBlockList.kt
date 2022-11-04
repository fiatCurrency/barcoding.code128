package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent

internal fun CharArray.buildBlockList(): List<Pair<CodeSet,Int>> {
    val output = ArrayList<Pair<CodeSet,Int>>()
    var index = 0
    while (index<this.size) {
        val codeSet = codeSetFromLetter(this[index])
        val extent = this.blockExtent(index)
        output.add(Pair(codeSet,extent))
        index += extent
    }
    return output
}

private fun codeSetFromLetter(type: Char): CodeSet {
    return when (type) {
        'A' -> CodeSet.Seta
        'B' -> CodeSet.Setb
        'C' -> CodeSet.Setc
        else -> throw IllegalArgumentException()
    }
}