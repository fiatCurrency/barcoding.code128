package uk.co.quantril.barcoding.code128.unescape.helpers

import uk.co.quantril.barcoding.code128.unescape.support.Reel

// Read an identifier, as a string of valid characters from the input, at the given position.
//   The valid characters as [A-Z] [a-z] [0-9]
//   The first character of an identifier must be [A-Z] [a-z] not [0-9]
//   The identifier ends when we reach the end of the input or we encounter a character
//     that is not valid for an identifier.
//   The identifier must start at the given position, with no preamble
//
//   If at least one valid character is encountered:
//     The index is incremented by exactly the number of characters in the identifier, so that
//       it points one beyond it. This is true whether parsing was terminated by non-eligible
//       character, or by exhausting the input
//     The passed function is invoked, with the parsed identifier as its parameter.
//     True is returned
//
//   If no valid character (or no character at all) is encountered:
//     The index is left unchanged
//     The passed function is not invoked
//     false is returned

internal fun Reel.readIdentifier(fn: (CharSequence)->Unit): Boolean {
    check(index >= 0 && index <= this.input.length)

    val nCharacters = countMatching { x->isValid2(x) }
    if (nCharacters==0) return false
    if (!isValid1(input[index])) return false
    check(inputRemainingIsAtLeast(nCharacters))

    fn(input.subSequence(index, index+nCharacters))
    advance(nCharacters)
    return true
}

internal fun Reel.readIdentifier(): CharSequence? {
    var identifier: CharSequence? = null
    if (this.readIdentifier { x -> identifier=x })
        check((identifier?.length ?: 0) > 0)
    else
        check(identifier==null)
    return identifier
}

private fun isValid1(c: Char): Boolean {
    return when (c) {
        in 'A'..'Z' -> true
        in 'a'..'z' -> true
        else -> false
    }
}

private fun isValid2(c: Char): Boolean {
    return (isValid1(c) || (c in '0'..'9'))
}