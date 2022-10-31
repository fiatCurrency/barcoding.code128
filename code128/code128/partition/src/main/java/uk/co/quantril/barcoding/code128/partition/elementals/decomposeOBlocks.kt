package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.helpers.alignSingleOBlock
import uk.co.quantril.barcoding.code128.partition.helpers.getLeftOrRightMating
import uk.co.quantril.barcoding.code128.partition.helpers.reverseMating
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock
import uk.co.quantril.barcoding.code128.partition.primitives.startOfBlockP
import uk.co.quantril.barcoding.code128.partition.primitives.theseTypesNotPresent

// Function to determine how to decompose a P or Q Block
//
// A P or Q block is a superblock representing a character sequence that was
//   originally represented as an O Block, optionally followed by one or more
//   (F Block, O Block) pairs. A P Block is where there were originally an odd number
//   of constituent O Blocks (including just one). A Q Block is where there were originally
//   an even number (not zero) of such.
//
// A P or Q block will eventually need to be decomposed into its constituent O and F blocks,
//   and, from there, into "single" digit (H) blocks and E (even number of digits) blocks.
//
// An O block of length, e.g. 7, will therefore eventually end up being represented as an H1 block
//   and an E6 block. However, the ordering ("H1 E6" versus "E6 H1") matters because of how well
//   said block-pair will "mate" with the blocks either side of it.
//
// "mating" refers to how easy it is to accommodate two blocks side by side, with a minimum
//   of code changing (Setab to/from Setc) to achieve an optimal encoding.
// For example, if the preceding block is an H4, then we have two possible encodings when
//   decomposing our O block: H5 E6  or H4 E6 H1.
// Including the start codes, the first encoding will require 10 code points in all, and the
//   second will require 11. The first is more efficient, but the difference is that the encoding
//   will be left in SetC (first) and Setab (second). The efficiency gain could be eliminated
//   by the following block not being of the "right" code set.
// We say that a block "H1 E6" has "H left mating" and "E right mating".
//
// Ideally, we want to pick the decomposition which mates correctly left AND right, but this
//   won't always be possible, as we can't pick what the user wants to encode !
// It can be shown, however, that, as long as the "right" OR the "left" mates favourably then,
//   although there may be other decompositions possible, they won't produce an encoding that is
//   any shorter. They may be longer.
//
// In practice then, we might as well mate the contained O blocks according to what precedes them.
//   However, this raises the question as to what to do if nothing precedes them (other than
//   an F block, which is considered simply a filler for this purpose). The answer is that
//   we then mate according to what block comes after the O block (again, passing any F block)
//   If we are in this situation of having to do right-mating, then we simply determine the
//   mating, and reverse it (as an O block will always start on the opposite mating to that
//   on which it ends).
//
// We are, of course, not just considering O blocks, but whole unbroken (other than by F blocks)
//   sequences of them. It can be shown that a P block will behave like an O block: whichever left
//   mating is used, the right mating will be the opposite. This is because there are an odd
//   number of O blocks making up a P block. With a Q block, however, there are an even number
//   of O blocks, so the right and left matings will be the same (either E E or H H).
//
// It can be shown that the left/right matings of O blocks within a P or Q block will
//   alternate E H -> H E -> E H ..., for each successive O block.
// A Q block exists simply so that if we decide to go for a right-mating with our superblock,
//   we can tell that the mating must not be reversed, to get the left-mating. If we only do
//   left-mating, the distinction between Q and P blocks would become irrelevant.
//
// The strategy for decomposing a P/Q block is thus:

// Determine what comes before it. This will be F H or nothing. It could also be a 'Z' for invalid
//   character or an 'E' for even block, but previous stages should not have yielded an E block
//   immediately followed by an O block.
//
// If it is F, then go back one more and inspect that block. That block may be H E or nothing.
// Again, it could be a 'Z'. It cannot be another F as two consecutive same-type blocks
//   can not be represented in the underlying character array.
//
// We should now end up with a left-mating of E or H. This will be the mating that we
//   will proceed to use when we come to really decompose the P/Q block.
//
// We could also end up with a left-mating of nothing; in this case, we have to inspect the
//   right-mating instead.
//
// The same procedure follows: inspect the successor block. It will be F H or nothing.
//
// If F, look to the block beyond that; it will be E H, or nothing
//
// We will now end up with a right-mating of E or H or nothing.
//   To determine the left-mating from this, we need to know whether we are in a P or a Q
//   block; for a Q block, the left-mating will be the same as the right-mating; for a P block,
//   it will be the opposite.
//
// If there is no viable block either side of our superblock (other than F blocks), we could
//   decompose it either way: we arbitrarily define this to use the "H E" mapping.
internal fun CharArray.decomposeOBlocks(reference: CharArray) {
    val receiver = this
    require(receiver.size == reference.size)
    var index = 0
    while (index < reference.size) {
        val extent = if (reference[index] in "PQ")
            alignByPQBlock(reference, index)
        else
            reference.seekNextBlock(index) - index
        check(extent != 0)
        index += extent
    }
    check(index == reference.size)
    check(theseTypesNotPresent("PQ"))
}

// Remap the O blocks indicated in the receiver array with reference to the P/Q Block present in
//   the array passed as referenceArray.
// The start position of that P/Q block is passed in the parameter start
private fun CharArray.alignByPQBlock(reference: CharArray, start: Int): Int {
    var index = start
    val receiver = this
    require(reference.size == receiver.size)
    require(index in 0 until size)
    require(reference.startOfBlockP(index, "PQ"))
    require(receiver.startOfBlockP(index, "O"))
    var mating = reference.getLeftOrRightMating(index, reverseRight = (reference[index] == 'P'))

    var index3: Int
    while (true) {
        index3 = index + receiver.alignSingleOBlock(index, mating)
        check(index3 in 0..size)
        if (index3 == receiver.size) break
        if (receiver[index3] != 'F') break
        val index4 = receiver.seekNextBlock(index3)
        if (!receiver.startOfBlockP(index4, "O")) break
        mating = reverseMating(mating)
        index = index4
    }
    check(reference.startOfBlockP(index3))
    return index3 - start
}

