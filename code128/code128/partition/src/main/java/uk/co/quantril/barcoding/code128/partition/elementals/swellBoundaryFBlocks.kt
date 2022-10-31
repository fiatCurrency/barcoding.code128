package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlock
import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent
import uk.co.quantril.barcoding.code128.partition.primitives.inBlockP
import uk.co.quantril.barcoding.code128.partition.primitives.rseekPreviousBlockEnd

// If there is an F Block at the very beginning of the array, make it the same type as
//   (thus part of) the block that follows.
// Also, if there is an F Block at the very end of the array, make it the same type as
//   (thus part of) the one that immediately precedes it
// If the F block has nothing before or after it (so it is the only block!), leave it
//   unchanged

internal fun CharArray.swellBoundaryFBlocks() {
    val last = size - 1
    val extentLeft = if (this.inBlockP(0,"F"))
        this.blockExtent(0) else 0
    val extentRight = if (this.isNotEmpty() && this[last] == 'F')
        last - this.rseekPreviousBlockEnd(last) else 0

    if (extentLeft in 1 until size)
        overwriteBlock(0, this[extentLeft])

    if (extentRight in 1 until size)
        overwriteBlock(size-extentRight, this[size-extentRight-1])
}

