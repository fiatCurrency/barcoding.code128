package uk.co.quantril.barcoding.code128.pointcode.helpers

import uk.co.quantril.barcoding.code128.code128support.CodeSet

internal const val startCodeA = 103
internal const val startCodeB = 104
internal const val startCodeC = 105
internal const val codeCodeA = 101
internal const val codeCodeB = 100
internal const val codeCodeC = 99
internal const val functionCode1 = 102
internal const val functionCode2ab = 97
internal const val functionCode3ab = 96
internal const val functionCode4a = 101
internal const val functionCode4b = 100

internal fun startCodeForSubset(subset: CodeSet): Int {
    return when(subset) {
        CodeSet.Seta -> startCodeA
        CodeSet.Setb -> startCodeB
        CodeSet.Setc -> startCodeC
    }
}

internal fun codeCodeForSubset(subset: CodeSet): Int {
    return when(subset) {
        CodeSet.Seta -> codeCodeA
        CodeSet.Setb -> codeCodeB
        CodeSet.Setc -> codeCodeC
    }
}
