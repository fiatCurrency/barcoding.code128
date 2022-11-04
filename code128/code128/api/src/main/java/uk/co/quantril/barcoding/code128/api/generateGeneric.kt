package uk.co.quantril.barcoding.code128.api

import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.unescape.unescape
import uk.co.quantril.barcoding.code128.partition.partitionToBlockList
import uk.co.quantril.barcoding.code128.encodation.encodeFromBlockList

internal fun generateGeneric(text: String, options: EncodingOptions): String {
    val textUe = unescape(text)
    val textBl = partitionToBlockList(textUe, options)
    val textEn = encodeFromBlockList(textBl, textUe, options)
    val seq = textEn.output().map { x -> x.toString().trim() }
    val output = seq.joinToString( separator=" ", prefix="[", postfix="]" )
    return output
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