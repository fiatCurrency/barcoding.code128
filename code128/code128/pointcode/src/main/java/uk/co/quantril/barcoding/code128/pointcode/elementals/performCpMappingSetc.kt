package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode1

internal fun Sequence<Pair<Int, CodeSet>>.performCpMappingSetc() = sequence {

    val iterator = this@performCpMappingSetc.iterator()

    while (iterator.hasNext()) {
        val datum1 = iterator.next()
        if (datum1.second != CodeSet.Setc) { yield(datum1); continue }
        if (datum1.first == Fnc.fnc1Code) { yield(Pair(functionCode1, CodeSet.Setc)); continue }

        require (datum1.first in 0x30..0x39 && iterator.hasNext())
        val datum2 = iterator.next()
        require (datum2.first in 0x30..0x39 && datum2.second == CodeSet.Setc)

        val number = (datum1.first-0x30)*10 + (datum2.first-0x30)
        yield (Pair(number, CodeSet.Setc))
    }
}
