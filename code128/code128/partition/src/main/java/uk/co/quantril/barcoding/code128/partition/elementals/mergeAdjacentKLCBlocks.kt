package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.inBlockP
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlockSaturating

// Join together any K,L,C blocks that are adjacent to each other. "adjacent", here
//   means separated by an F block.
// The resulting block will cover the exact region occupied by the original three blocks
//   and will have its type promoted if necessary to accommodate the total number of
//   digit-pairs.
// We specifically exclude F blocks at the very end and/or beginning of the array, as
//   they necessarily can't have neighbouring blocks on both sides.
// We specifically exclude F blocks that have neighbours but where at least one of those
//   is not in the set "KLC"
// It is not required that the KLC blocks either side of the F block be the same, only
//   they both be in the set "KLC"
internal fun CharArray.mergeAdjacentKLCBlocks() {
    var ix1 = 0
    var ix2 = this.seekNextBlockSaturating(ix1)
    while (ix1 < this.size) {
        val ix3 = this.seekNextBlockSaturating(ix2)
        if (inBlockP(ix1, "KLC") && inBlockP(ix2, "F") && inBlockP(ix3, "KLC")) {
            val ix4 = this.seekNextBlockSaturating(ix3)
            val overwrite = if (this[ix1] == 'K' && this[ix3] == 'K') 'L' else 'C'
            for (index in ix1 until ix4) {
                this[index] = overwrite
            }
            // We could optimise the position of ix1 here.
            ix2 = ix4
        } else {
            ix1 = ix2
            ix2 = ix3
        }
    }
}

