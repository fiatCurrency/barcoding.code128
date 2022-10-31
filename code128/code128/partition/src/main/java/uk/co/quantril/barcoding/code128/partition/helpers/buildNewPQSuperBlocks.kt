package uk.co.quantril.barcoding.code128.partition.helpers

import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent
import uk.co.quantril.barcoding.code128.partition.primitives.seekBlock
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlockSaturating

// Replace each longest run of consecutive O blocks with either a P or Q block, according to
//   whether the number of constituent O blocks is odd or even. O blocks are consecutive if
//   there is an F block (of any length >=1) separating them
// OOOOO -> PPPPP
// OOOOOFOOO -> QQQQQQQQQ
// OOOFFOOOFOOO -> PPPPPPPPPPPP
//
// Replace O+ with P+
// Replace O+ (F+ O+ F+ O+)* with P+
// Replace O+ F+ O+ (F+ O+ F+ O+)* with Q+
internal fun buildNewPQSuperblocks(ca: CharArray): CharArray {
    val ca2 = ca.clone()    // Defensive copy
    var index = 0
    do {
        index = ca2.gatherOdds2(index)
    } while (index < ca2.size)
    check(index == ca2.size)
    return ca2
}

private fun CharArray.gatherOdds2(start: Int): Int {
    var countIsEven = true  // a zero-length O block is considered even
    var extent: Int
    var oBlock = this.seekBlock(start,'O')
    val firstOBlock = oBlock
    var nbs = this.size
    if (oBlock < this.size) {
        extent = this.blockExtent(oBlock)
        while(true) {
            val block2 = this.seekNextBlockSaturating(oBlock)
            val block3 = this.seekNextBlockSaturating(block2)
            val block4 = this.seekNextBlockSaturating(block3)
            if (oBlock==this.size || block2==this.size || block3==this.size) break
            check(this[oBlock]=='O')
            if (this[block2] != 'F' || this[block3] != 'O') break
            extent += (block4 - block2)
            countIsEven = !countIsEven
            oBlock = block3
        }
        val replacement = if (countIsEven) 'Q' else 'P'
        nbs = firstOBlock + extent
        for (i in firstOBlock until nbs)
            this[i] = replacement
    }
    return nbs
}
