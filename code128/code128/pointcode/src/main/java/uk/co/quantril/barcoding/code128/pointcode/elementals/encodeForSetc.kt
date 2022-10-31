package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode1

// Compress digit-pairs that are to be encoded in Setc, into a single code point
//   fnc1 codes that are to be encoded in SetC are passed unchanged
//   items to be encoded in any set other than SetC are passed unchanged
// Everything else must be a digit-pair where both are to be encoded in Setc.
//   If the above does not hold (not both digits, wrong subset), then
//   an InvalidArgumentException is thrown
internal fun Sequence<Pair<Int, CodeSet>>.encodeForSetc() = sequence {
    val iterator = iterator()
    while (iterator.hasNext()) {
        val data1 = iterator.next()
        if (data1.second == CodeSet.Setc) {
            if (data1.first == Fnc.fnc1Code) {
                yield(Pair(functionCode1, data1.second))
            } else {
                require(data1.first in 0x30..0x39)
                require(iterator.hasNext())
                val data2 = iterator.next()
                require(data2.first in 0x30..0x39)
                require(data2.second == CodeSet.Setc)
                val codepoint = 10 * (data1.first - 0x30) + (data2.first - 0x30)
                yield(Pair(codepoint, CodeSet.Setc))
            }
        } else
            yield(data1)
    }
}