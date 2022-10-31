package uk.co.quantril.barcoding.code128.partition.helpers

import uk.co.quantril.barcoding.code128.partition.primitives.rseekPreviousBlockEnd
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock

private fun CharArray.getLeftMatingIndex(index: Int): Int {
    var sob = rseekPreviousBlockEnd(index)
    if (sob >= 0 && this[sob] == 'F')
        sob = rseekPreviousBlockEnd(sob)
    return sob
}

private fun CharArray.getRightMatingIndex(index: Int): Int {
    var fbs = seekNextBlock(index)
    if (fbs < this.size && this[fbs] == 'F')
        fbs = seekNextBlock(fbs)
    return fbs
}

private fun CharArray.matingFromIndex(index: Int): Int {
    return if (index >= 0 && index < this.size) (
            when (this[index]) {
                'H' -> 1
                'E' -> 2
                else -> -1
            }
            ) else
        0
}

private fun CharArray.getLeftMating(index: Int): Int {
    return matingFromIndex(getLeftMatingIndex(index))
}

private fun CharArray.getRightMating(index: Int): Int {
    return matingFromIndex(getRightMatingIndex(index))
}

internal fun CharArray.getLeftOrRightMating(
    index: Int,
    reverseLeft: Boolean = false,
    reverseRight: Boolean = false
): Int {
    var mating = getLeftMating(index)
    var reversalFlag = reverseLeft
    check(mating in 0..2)
    if (mating == 0) {
        reversalFlag = reverseRight
        mating = getRightMating(index)
        check(mating in 0..2)
        if (mating == 0) {
            reversalFlag = false
            mating = 1
        }
    }
    return if (reversalFlag) reverseMating(mating) else mating
}

internal fun reverseMating(m: Int): Int {
    require(m in 1..2)
    return 3 - m
}