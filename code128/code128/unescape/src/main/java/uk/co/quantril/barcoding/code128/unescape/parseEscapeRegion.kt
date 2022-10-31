package uk.co.quantril.barcoding.code128.unescape

import uk.co.quantril.barcoding.code128.unescape.support.Reel
import uk.co.quantril.barcoding.code128.unescape.support.backtick

// Process an Escape Region.
// Entry conditions: Must already be 'on' a backtick character
// Action: process the Escape sequence(s) between this and the closing backtick
//   (exclusive), consuming the Escape sequences, and writing their corresponding
//   substitutions to the output.
//   Verify that there is a backtick at the end of the Escape sequences, and
//   consume it.
// Return value:
//   If all the enclosed Escape sequences were valid, and there was a correct
//   closing backtick, then true will be returned, and the index will be at the
//   character following that closing backtick.
//   If there was an error parsing the above (including the lack of a closing backtick)
//   false will be returned, and the index will be at the start of the offending
//   substitution.
// Process an Escape section.
// Entry conditions: we must be already 'on' the first character of the Escape
//   section. In practice, this will mean one beyond the opening backtick.
// Action: parse the incoming text for substitution patterns, passing any spaces before
//   and/or afterwards; interpret each such substitution pattern appropriately,
//   consume, and pass the interpretation to the output.
//   Keep doing this until we reach a closing backtick.
// Return value:
//   If we manage to parse all the substitutions without error, and successfully
//   consume the closing backtick, then true is returned, with the index 'on'
//   the closing backtick.
//   If any substitution should fail, or we reach the end of input before the
//   backtick, then false will be returned, and the index will be left at the
//   position of the offending text (which will be end-of-text in the case of
//   a missing closing backtick)

internal fun Reel.parseEscapeRegion(): Boolean {
    require(atHead(backtick))
    consumeHead()
    while(!atHead(backtick)) {
        this.skip(' ')
        if (!parseSubstitution()) return false
    }
    consumeHead()
    return true
}


