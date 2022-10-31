package uk.co.quantril.barcoding.code128.partition.helpers

import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent
import uk.co.quantril.barcoding.code128.partition.primitives.startOfBlockP

// Remap the O block indicated into an E block (even length) with an H block (of length 1)
//   either at the beginning(1) or end(2) of that block, according to the passed mode parameter.
// The parameter index must point to the start of an O block
internal fun CharArray.alignSingleOBlock(index: Int, mode: Int): Int {
    require(index >= 0 && index < this.size && mode in 1..2)
    require(this.startOfBlockP(index, "O"))
    val nDigits = this.blockExtent(index)
    require(nDigits > 0 && nDigits % 2 == 1)
    for (i in index until index + nDigits) {
        this[i] = 'E'
    }
    this[index + if (mode == 1) 0 else (nDigits - 1)] = 'H'
    return nDigits
}