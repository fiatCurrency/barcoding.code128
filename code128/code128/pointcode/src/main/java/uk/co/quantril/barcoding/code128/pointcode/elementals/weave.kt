package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc

// Take a sequence representing the block types (from lex) and a sequence representing the
//   characters to be encoded (from parse) and present them as a sequence of Pair<Int,Int>
//   values.
// Both incoming sequences must be the same length.
// The block types must all be one of 'A', 'B', or 'C'
// The characters must all be in the ASCII range 0x00 to 0x7F or one of the fncX private use
//   characters.
// All the above are assumed to have been enforced by the caller; any breach will cause
//   an IllegalArgumentException()
internal fun weave(subsets: Sequence<Char>, data: Sequence<Char>) = sequence {
    val iterator = subsets.iterator()
    data.forEach {
        require(iterator.hasNext())
        yield(Pair(validatedCodePoint(it.code), fromLetter(iterator.next())))
    }
    require(!iterator.hasNext())
}

private fun validatedCodePoint(code: Int): Int {
    return if (when(code) {
        Fnc.fnc1Code, Fnc.fnc2Code, Fnc.fnc3Code, Fnc.fnc4Code -> true
        in 0x00..0x7F -> true
        else -> false
    }) code else throw IllegalArgumentException()
}

private fun fromLetter(letter: Char): CodeSet {
    return when(letter) {
        'A' -> CodeSet.Seta
        'B' -> CodeSet.Setb
        'C' -> CodeSet.Setc
        else -> throw IllegalArgumentException()
    }
}