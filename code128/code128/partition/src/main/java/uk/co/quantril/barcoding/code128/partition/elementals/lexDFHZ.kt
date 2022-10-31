package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent
import uk.co.quantril.barcoding.code128.partition.primitives.onlyTheseTypesPresent
import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlock
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock

internal fun lexDFHZ(string: String): CharArray {
    val ca = CharArray(string.length) { i ->
        when (string[i]) {
            in digits -> 'D'
            Fnc.fnc1 -> 'F'
            Fnc.fnc2, Fnc.fnc3, Fnc.fnc4 -> 'H'
            in sevenBitAscii -> 'H'
            else -> 'Z'
        }
    }
    var start = 0
    while (start < ca.size) {
        start = if (ca[start] == 'D') {
            ca.overwriteBlock(start,ca.blockExtent(start).let {
                if (it==1) 'H' else if (it%2==0) 'E' else 'O' })
        } else
            ca.seekNextBlock(start)
    }
    check(ca.onlyTheseTypesPresent("EOFHZ"))
    return ca
}

private val digits = '0'..'9'
private val sevenBitAscii = 0x00.toChar()..0x7F.toChar()