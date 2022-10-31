package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode1
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode2ab
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode3ab
import uk.co.quantril.barcoding.code128.pointcode.helpers.functionCode4a

internal fun Sequence<Pair<Int, CodeSet>>.performCpMappingSeta() = sequence {
    this@performCpMappingSeta.forEach() {
        if (it.second != CodeSet.Seta)
            yield(it)
        else yield(
            Pair (
                when (it.first) {
                    in 0x00..0x1F -> it.first + 0x40    // 0x40 to 0x5F
                    in 0x20..0x5F -> it.first - 0x20    // 0x00 to 0x3F
                    Fnc.fnc1Code -> functionCode1
                    Fnc.fnc2Code -> functionCode2ab
                    Fnc.fnc3Code -> functionCode3ab
                    Fnc.fnc4Code -> functionCode4a
                    else -> throw IllegalStateException()
                },
                CodeSet.Seta
            )
        )
    }
}
