package uk.co.quantril.barcoding.code128.unescape

import uk.co.quantril.barcoding.code128.code128support.Fnc
import uk.co.quantril.barcoding.code128.unescape.helpers.readHexFixed
import uk.co.quantril.barcoding.code128.unescape.helpers.readIdentifier
import uk.co.quantril.barcoding.code128.unescape.support.Reel

internal fun Reel.parseSubstitution(): Boolean {
    return (
        parseHexSubstitution("0x",2) ||
        parseHexSubstitution("U+",4) ||
        parseC0ControlCode() ||
        parsePairedCode(controlCodeArray2) ||
        parsePairedCode(functionCodeArray)
    )
}

private fun Reel.parseC0ControlCode(): Boolean {

    val controlCodeC0Array: Array<String> = arrayOf(
        "NUL","SOH","STX","ETX","EOT","ENQ","ACK","BEL","BS", "HT", "LF", "VT",
        "FF", "CR", "SO", "SI", "DLE","DC1","DC2","DC3","DC4","NAK","SYN","ETB",
        "CAN","EM", "SUB","ESC","FS", "GS", "RS", "US")

    val index0 = index
    return controlCodeC0Array.indexOf(this.readIdentifier()?:"").let {
        if (it!=-1) {
            check(it in 0x00..0x1F)
            check(index>index0)
            output.append(it.toChar())
            true
        } else {
            index=index0
            false
        }
    }
}

private val controlCodeArray2: Array<Pair<String,Char>> = arrayOf(
    Pair("SPC",0x20.toChar()), Pair("DEL",0x7F.toChar())
)

private val functionCodeArray: Array<Pair<String,Char>> = arrayOf(
    Pair("F1", Fnc.fnc1), Pair("F2", Fnc.fnc2), Pair("F3", Fnc.fnc3), Pair("F4", Fnc.fnc4),
    Pair("FNC1", Fnc.fnc1), Pair("FNC2", Fnc.fnc2), Pair("FNC3", Fnc.fnc3), Pair("FNC4", Fnc.fnc4),
    Pair("1", Fnc.fnc1), Pair("2", Fnc.fnc2), Pair("3", Fnc.fnc3), Pair("4", Fnc.fnc4)
)

private fun Reel.parsePairedCode(array: Array<Pair<String,Char>>): Boolean {
    val index0 = index
    readIdentifier() ?.let {
        ( array.firstOrNull { (s,_) -> (s==it) } )?.let {
            check(index>index0)
            output.append(it.second)
            return true
        }
    }
    index = index0
    return false
}

private fun Reel.parseHexSubstitution(prefix: CharSequence, nDigits: Int): Boolean {
    return readHexFixed(prefix,nDigits) { x->this.output.append(x.toChar()) }
}
