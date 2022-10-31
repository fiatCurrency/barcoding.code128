package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.pointcode.helpers.startCodeForSubset

// Provide the sequence with a start code in the case where it doesn't have one (which would
//   mean the sequence was totally empty)
internal fun Sequence<Int>.ensureStartCode(defaultSet: CodeSet) =
    this@ensureStartCode.ifEmpty() {
        sequence { yield(startCodeForSubset(defaultSet)) }
    }
