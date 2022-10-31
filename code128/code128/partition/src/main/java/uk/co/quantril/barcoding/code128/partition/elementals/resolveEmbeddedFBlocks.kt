package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlockIfNonZero
import uk.co.quantril.barcoding.code128.partition.primitives.seekBlock
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock
import uk.co.quantril.barcoding.code128.partition.primitives.zeroBlock

internal fun CharArray.resolveEmbeddedFBlocks() {
    var index = 0
    while (index < this.size)
        index = this.processBlock(this.seekBlock(index, 'F'))
}

// Overwrite the passed block with the same block type as its neighbours.
//   If there are two neighbours, and they are the same type, overwrite.
//   If there is only one neighbour (left XOR right), overwrite with that single neighbour's type.
//   If there are no neighbours, do not overwrite.
//   If there are two neighbours, but they are of different types, do not overwrite.
//
// Return value is one beyond the end of the passed block (which might be this.size) as
//   it was when passed in, even given that that block may now have merged with its
//   neighbours.
private fun CharArray.processBlock(ix: Int): Int {
    require(ix >= 0 && ix <= this.size)
    return (
        if (ix < this.size) {
            val nextBlock = this.seekNextBlock(ix)
            val predecessor = if (ix > 0) this[ix - 1] else zeroBlock
            val successor = if (nextBlock < this.size) this[nextBlock] else zeroBlock
            val newType =
                if (successor == zeroBlock && predecessor == zeroBlock) zeroBlock
                else if (successor == zeroBlock) predecessor
                else if (predecessor == zeroBlock) successor
                else if (predecessor == successor) predecessor
                else /* if (predecessor!=successor) */ zeroBlock
            this.overwriteBlockIfNonZero(ix, newType)
        } else {
            require(ix == this.size)
            ix
        }
    )
}