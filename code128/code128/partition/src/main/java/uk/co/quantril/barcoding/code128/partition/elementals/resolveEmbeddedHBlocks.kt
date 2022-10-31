package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlockIfNonZero
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock

private const val zeroChar = 0.toChar()

internal fun CharArray.resolveEmbeddedHBlocks() {
    var index = 0
    while (index < this.size)
        index = performReplacementIfNonZero(index, processBlock(index))
}

private fun CharArray.performReplacementIfNonZero(index: Int, char: Char): Int {
    require(index>=0 && index<this.size)
    return this.overwriteBlockIfNonZero(index,char)
}

// Return the character with which the passed block is to be overwritten, based on the
//   types of the blocks before and after
private fun CharArray.processBlock(index: Int): Char {
    require(index in 0 until size)
    return if (this[index] != 'H')
        zeroChar
    else {
        val indexNext = seekNextBlock(index)

        val typeNext = if (indexNext == this.size) zeroChar else this[indexNext]
        val typeBefore = if (index == 0) zeroChar else this[index - 1]

        if (typeNext == zeroChar && typeBefore in "AB") typeBefore
        else if (typeNext == zeroChar && typeBefore == zeroChar) typeNext
        else if (typeBefore == zeroChar && typeNext in "AB") typeNext
        else if (typeBefore == typeNext && typeNext in "AB") typeNext
        else if (typeBefore in "AB" && typeNext in "AB" && typeBefore != typeNext) {

            // This is where we have an A block one side and a B block the other. We
            //   have to see which is "longer". The only measure of "longer" is whether its
            //   length is 1 or (2 or more)
            check(index>0 && indexNext<this.size)
            val beforeMagnitude = if (index>1 && this[index-2]==typeBefore) 2 else 1
            val afterMagnitude = if (indexNext<this.size-1 && this[indexNext+1]==typeNext) 2 else 1
            if (beforeMagnitude>afterMagnitude) typeBefore
            else if (afterMagnitude>beforeMagnitude) typeNext
            else zeroChar

        } else {
            // There is a non-zero block either before or after, and it is neither A nor B
            //   This means that we have no valid replacement for it
            zeroChar
        }
    }
}
