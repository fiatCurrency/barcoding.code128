package uk.co.quantril.barcoding.code128.unescape

import uk.co.quantril.barcoding.code128.unescape.support.Reel
import uk.co.quantril.barcoding.code128.unescape.support.backtick

// TODO. This comment is now out of date.
// Take the passed string, and return that string with any embedded substitution
//   sequences pertinent to Code128 interpreted
// These substitution sequences are embedded in pairs of backticks (ASCII 96). They are:
//   `F1` Code128 func1 function code
//     (similarly `F1`,`F2`,`F3`)
//   `BEL` ASCII function code 0x07
//     (similarly `NUL` through to `US`, 0x00 to 0x1F)
//     (similarly `DEL` for ASCII 0x7F)
//     (similarly `SPC` for an ASCII space, 0x20)
//   `U+xxxx` (four x characters) to represent the Unicode character with that hexadecimal
//     number.
// A single backtick can be encoded by doubling it or by using, within backticks, 0x60 or U+0060
//
// Multiple substitution codes can be enclosed within a single pair of backticks, separated
//   by spaces. Additional spaces can be used to aid readability between/before/after
//   substitution tokens, but can not be used within a substitution token.
//
// ABC`F1`F1
//   will yield six characters: the first three will be "ABC"; the fourth will be the Code128 fnc1
//     character; the fifth will be a normal "F"; the sixth will be a normal digit "1"
//
// `CR LF`
// `CR``LF`
// `   CR    U+000A  `
// ` U+000D``   U+000A '
//
// All the above four will yield the same: a two-character string, the first character of which
//   is ASCII 13 (carriage return), and the second of which is ASCII 10 (line feed)
//
// BJ``YZ
// BJ'U+0060'YZ
//   The above two will both yield the same five-character sequence, the middle of which is the
//     backtick character.
//
// Code 128 fncX codes will map to their code points in the Unicode private usage area
//
// The successful parsing of such a code does not guarantee that the resulting string can be
//   encoded in Code 128, for several reasons:
//      No check is performed that the resulting string's characters are all in the range
//        0x00 to 0x7F, which is what Code 128 requires
//      No check is made for digits being properly paired (as would be required in an environment
//        that uses Setc exclusively)
//      No check is made for digits that need to be encoded in SetA (or SetB) if used in an
//        environment where the required code subset is not enabled
//
// There is no provision in this system for embedding the various set-change codes (StartABC,
//   CodeABC, Shift, Stop, Checksum) within the string to be encoded. These simply do not fit
//   in at this level.

public fun unescape(input: CharSequence): String {
    val reel = Reel(input)
    if (!parse(reel)) {
        throw IllegalStateException()   // TODO. Not a good way to exit on a syntax error!
    }
    return reel.output.toString()
}

private fun parse(reel: Reel): Boolean {
    var previousIndex: Int
    do {
        previousIndex = reel.index
        if (!reel.parse1()) return false
    } while(reel.index>previousIndex)
    return reel.inputExhausted()
}

private fun Reel.parse1(): Boolean {
    while (inputRemains()) {
        if (!atHead(backtick))
            output.append(consumeHead())
        else if (!parseEscapeRegion())
            return false
    }
    return inputExhausted()
}

