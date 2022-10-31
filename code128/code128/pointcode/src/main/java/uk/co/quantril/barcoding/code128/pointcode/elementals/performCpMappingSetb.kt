package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode1
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode2ab
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode3ab
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode4b

internal fun Sequence<Pair<Int, CodeSet>>.performCpMappingSetb() = sequence {
    this@performCpMappingSetb.forEach {
        if (it.second != CodeSet.Setb)
            yield(it)
        else yield(
            Pair (
                when (it.first) {
                    in 0x20..0x7F -> it.first - 0x20
                    Fnc.fnc1Code -> functionCode1
                    Fnc.fnc2Code -> functionCode2ab
                    Fnc.fnc3Code -> functionCode3ab
                    Fnc.fnc4Code -> functionCode4b
                    else -> throw IllegalStateException()
                },
                CodeSet.Setb
            )
        )
    }
}
