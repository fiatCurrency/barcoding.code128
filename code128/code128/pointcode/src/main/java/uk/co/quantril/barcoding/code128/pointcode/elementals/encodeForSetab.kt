package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.pointcode.helpers.*
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode1
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode2ab
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode3ab

// Perform encodation of code points in subsets A and B, checking that the code is valid for
//   that particular subset.
internal fun Sequence<Pair<Int, CodeSet>>.encodeForSetab() = sequence {
    iterator().forEach {
        val subset = it.second
        val code: Int = it.first
        if (subset==CodeSet.Seta || subset==CodeSet.Setb) {
            val outcode = when(code) {
                Fnc.fnc1Code -> functionCode1
                Fnc.fnc2Code -> functionCode2ab
                Fnc.fnc3Code -> functionCode3ab
                Fnc.fnc4Code -> if (subset==CodeSet.Seta) functionCode4a else functionCode4b
                in 0x20..0x5F -> (code-0x20)
                in 0x00..0x1F -> if (subset==CodeSet.Seta) (code+0x40) else -1
                in 0x60..0x7F -> if (subset==CodeSet.Setb) (code-0x20) else -1
                else -> -1
            }
            require(outcode>=0)
            yield(Pair(outcode,subset))
        }
        else
            yield(it)
    }
}