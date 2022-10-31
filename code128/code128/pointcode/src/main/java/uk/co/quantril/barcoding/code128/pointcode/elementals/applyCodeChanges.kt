package uk.co.quantril.barcoding.code128.pointcode.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.pointcode.helpers.codeCodeForSubset
import uk.co.quantril.barcoding.code128.pointcode.helpers.startCodeForSubset

// Output the encodations, issuing a new code-change code when this changes.
internal fun Sequence<Pair<Int, CodeSet>>.applyCodeChanges() = sequence {

    val iterator = this@applyCodeChanges.iterator()

    if (iterator.hasNext()) {
        val head = iterator.next()
        var subset = head.second
        yield(startCodeForSubset(subset))
        yield(head.first)
        while (iterator.hasNext()) {
            val datum = iterator.next()
            if (datum.second != subset) {
                subset = datum.second
                yield(codeCodeForSubset(subset))
            }
            yield(datum.first)
        }
    }
}

