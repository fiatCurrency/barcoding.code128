package uk.co.quantril.barcoding.code128.partition.helpers

import uk.co.quantril.barcoding.code128.code128support.CodeSet

internal fun blockTypeFromCodeSet(cs: CodeSet): Char {
    return when(cs) {
        CodeSet.Seta -> 'A'
        CodeSet.Setb -> 'B'
        CodeSet.Setc -> 'C'
    }
}