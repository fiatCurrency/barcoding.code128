package uk.co.quantril.barcoding.code128.api

import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.unescape.unescape
import uk.co.quantril.barcoding.code128.partition.partition
import uk.co.quantril.barcoding.code128.pointcode.pointcode

internal fun generateGeneric(text: String, options: EncodingOptions): String {
    val textUe = unescape(text)
    val textPr = partition(textUe, options)
    val pcList = pointcode(textUe, textPr, options.setWhenEmpty)
    /*
    val en = encode(ca.toString(),text2)
    val en2 = en.map { x -> x.toString().trim() }
    val en3 = en2.joinToString(separator=" ",prefix="[",postfix="]")
    */

    return pcListToString(pcList)
}

private fun pcListToString(pca: List<Int>): String {
    val sb = StringBuilder()
    var first = true
    pca.forEach {
        if (!first) sb.append(" ")
        sb.append(it.toString().trim())
        first = false
    }
    return sb.toString()
}