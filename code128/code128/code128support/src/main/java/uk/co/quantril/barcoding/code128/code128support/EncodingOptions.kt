package uk.co.quantril.barcoding.code128.code128support

public data class EncodingOptions(
    val modeSetc: SetcPolicy,
    val preferSetaOverSetb: Boolean,
    val setWhenJustFnc1: CodeSet,
    val setWhenEmpty: CodeSet
)

