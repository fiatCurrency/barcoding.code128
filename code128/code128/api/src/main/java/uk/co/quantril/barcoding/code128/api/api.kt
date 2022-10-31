package uk.co.quantril.barcoding.code128.api

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.code128support.SetcPolicy

public fun generate(text: String): String {
    return generateGeneric(text, eoShorter)
}

private val eoShorter = EncodingOptions(
    SetcPolicy.IfShorter,
    false, CodeSet.Setc, CodeSet.Setb
)