package uk.co.quantril.barcoding.code128.unescape.helpers

import uk.co.quantril.barcoding.code128.unescape.support.Reel

internal fun Reel.seekBeyondPrefix(prefix: CharSequence): Boolean {
    require(prefix.isNotEmpty())
    if (!inputRemainingIsAtLeast(prefix.length)) return false
    for (i in prefix.indices) {
        if (prefix[i] != input[index+i]) return false
    }
    advance(prefix.length)
    return true
}
