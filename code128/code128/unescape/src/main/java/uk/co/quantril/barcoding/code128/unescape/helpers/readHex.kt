package uk.co.quantril.barcoding.code128.unescape.helpers

import uk.co.quantril.barcoding.code128.unescape.support.Reel

// Read a minimum of one and a maximum of maxDigits hexadecimal digits from the
//   input, at the current position.
// Return conditions:
//   If at least one digit is successfully parsed as hex:
//     the input index is stepped to one after the last digit parsed.
//     the function fn() is invoked on the Int built-up by the parse.
//     true is returned.
//   If no digit is successfully parsed as hex:
//     the input index is not changed.
//     the function fn() is not invoked.
//     false is returned.
// Return true iff at least one digit was successfully parsed.
// If true is returned, the embedded function fn is invoked on that parsed value
internal fun Reel.readHex(maxDigits: Int, fn: (Int)->Unit): Boolean {
    require(maxDigits>=1)
    var nDigits = 0
    var value = 0

    while (nDigits<maxDigits) {
        if (index+nDigits==this.input.length) break
        val h = hexChar(input[index + nDigits])
        if (h == -1) break
        check(h in 0..15)
        value = value * 16 + h
        ++nDigits
    }
    return if (nDigits==0) {
        false
    } else {
        index += nDigits
        fn(value)
        true
    }
}

private fun hexChar(c: Char): Int {
    return when (c) {
        in '0' .. '9' -> c - '0'
        in 'A' .. 'F' -> c - 'A' + 10
        in 'a' .. 'f' -> c - 'a' + 10
        else -> -1
    }
}