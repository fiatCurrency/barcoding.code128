package uk.co.quantril.barcoding.code128.partition.primitives

// Return the number of elements in the block, from index to the end of the block,
//   inclusive.
// If index is the start of the block, then the extent (value returned) will be the
//   same as the length of the block.
// The extent of a block must always be at least 1
// If index is -1, then the extent (returned value) will be 1
// If index is the positive overrun value, an exception is thrown because that represents
//   an infinitely long unnamed block
internal fun CharArray.blockExtent(index: Int): Int {
    return this.seekNextBlock(index) - index
}