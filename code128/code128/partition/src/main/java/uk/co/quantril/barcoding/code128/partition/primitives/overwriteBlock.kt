package uk.co.quantril.barcoding.code128.partition.primitives

// Overwrite an existing block within the array with a new block type.
// The passed index must point within the valid part of the array and must point to
//   the start of a block.
// The overwrite operation may cause the previous block to become one with the indicated block,
//   and/or the following block to become one with the indicated block.
internal fun CharArray.overwriteBlock(toIndex: Int, c: Char): Int {
    require(toIndex >= 0 && toIndex <= size-1)
    require(toIndex == 0 || this[toIndex] != this[toIndex - 1])
    var index = toIndex
    val type = this[index].toString()
    while (inBlockP(index,type))
        this[index++] = c
    return index
}

internal fun CharArray.overwriteBlockIfNonZero(toIndex: Int, c: Char): Int {
    return if (c== zeroBlock)
        this.seekNextBlock(toIndex)
    else
        this.overwriteBlock(toIndex, c)
}