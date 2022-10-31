package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlock
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock


// A "C" block is one that has already been determine as being more efficient to encode in Setc
// A "L" block is one that would be /equally/ efficient in SetC, and would need one more
//   digit-pair to make it /more/ efficient in Setc
// A "K" block would need one more digit-pair, to become equally efficient in Setc, and a further
//   digit-pair to be /more/ efficient in Setc
// However, the above relations don't hold at the very beginning/end of the String:
// A block at the beginning of the String won't need a code-change to go into Setc, as there would
//   already be a start-code C immediately preceding
// A block at the end of the String won't need a code-change to change back to Setab, as there are
//   no more characters to encode
// (Any F blocks at the extreme ends of the String are assumed to already have been merged with
//   K,L,C blocks adjacent to them (if any), so that said K,L,C blocks supplant the erstwhile F
//   blocks by absorbing it.
// As a result, a K block can be promoted to an L block simply by being at the beginning of
//   the String. The same is true if it is at the extreme end of the String. If it is both at the
//   beginning and at the end of the String, it must be the whole String, and can be promoted to
//   a C block.
// Similarly, an L block at the beginning or end of the String can be promoted to a C block.
//   If it is both at the beginning and at the end of the String, it can be double promoted, but
//   there's no point, as there is nowhere to go beyond a C block.
internal fun CharArray.promoteBoundaryKLCBlocks() {
    var index = 0
    while (index < size) {
        val next = this.seekNextBlock(index)
        val type = this[index]
        if (type in "KL") {
            var rank = if (type=='L') 1 else 0
            if (index == 0) rank++
            if (next == size) rank++
            val newType = "KLCC"[rank]
            if (newType != type) this.overwriteBlock(index, newType)
        }
        index = next
    }
}
