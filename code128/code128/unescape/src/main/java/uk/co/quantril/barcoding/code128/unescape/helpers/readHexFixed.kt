package uk.co.quantril.barcoding.code128.unescape.helpers

import uk.co.quantril.barcoding.code128.unescape.support.Reel

internal fun Reel.readHexFixed(prefix: CharSequence, nDigits: Int, fn: (Int)->Unit)
    : Boolean {
    val oldIndex = index
    var number = 0
    return if (this.rhf2(prefix, nDigits) { x:Int -> number = x }) {
        fn(number)
        true
    } else {
        index = oldIndex
        false
    }
}

private fun Reel.rhf2(prefix: CharSequence, nDigits: Int, fn: (Int)->Unit)
    : Boolean {
    var parsedHex = 0

    if (!seekBeyondPrefix(prefix)) return false
    if (!inputRemainingIsAtLeast(nDigits)) return false
    val ix1 = this.index
        // We are looking for an exact number of digits, rather than a "maximum", so
        // we allow to read one more digit, to detect overrun (!)
    if (!this.readHex(nDigits+1) { x -> parsedHex=x }) return false
    if (index-ix1 != nDigits) return false  // too short or too long !

    fn(parsedHex)
    return true
}