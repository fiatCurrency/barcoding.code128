package uk.co.quantril.barcoding.code128.partition.primitives

// Test whether the block indicated by the passed index is of one of types
//   passed in String candidates
// There is no requirement that index be the start of a block; it can be anywhere
//   within the extent of any block.
// The return value will be false if:
//   the candidates String is empty
//   the receiver CharArray is empty
//   the passed index is -1 or is the size of the array
//   the character at the passed index is not one of those in the candidates String
internal fun CharArray.inBlockP(index: Int, candidates: String): Boolean {
    require(index >= -1 && index <= this.size)
    return (index >= 0 && index < this.size && this[index] in candidates)
}

// Test whether the passed index is at the start of a block, rather that at
//   some other point within its extent.
// The positive overrun (index==size) is considered to be the start of an infinitely
//   long unnamed block, so the return value will be true for (index==size)
// The negative overrun (index==-1) is considered to be the end of an infinitely
//   long unnamed block, so it can never be the start of a block. It is expected
//   that this condition be checked for before invoking this function: if the
//   value -1 is passed as index, an exception will be thrown.
internal fun CharArray.startOfBlockP(index: Int): Boolean {
    require(index >= 0 && index <= this.size)
    return (index == 0 || index == this.size || this[index] != this[index - 1])
}

// Test whether the passed index is at the start of a block whose type is one
//   of the elements of the passed candidates String.
// This works as its single-parameter variant, but then tests additionally
//   whether that block is of one of the candidate block types.
// The positive overrun is assumed to be an unnamed block type, so false will
//   be returned in the case of this being passed as index
// If the candidate String is empty, then false will be returned.
internal fun CharArray.startOfBlockP(index: Int, candidates: String): Boolean {
    val rv = this.startOfBlockP(index)
    return (rv && index < this.size && this[index] in candidates)
}