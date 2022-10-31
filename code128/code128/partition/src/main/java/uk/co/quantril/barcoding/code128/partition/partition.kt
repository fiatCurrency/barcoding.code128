package uk.co.quantril.barcoding.code128.partition

import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.partition.elementals.*
import uk.co.quantril.barcoding.code128.partition.helpers.buildNewPQSuperblocks
import uk.co.quantril.barcoding.code128.partition.primitives.onlyTheseTypesPresent
import uk.co.quantril.barcoding.code128.partition.primitives.theseTypesNotPresent

public fun partition(text: String, options: EncodingOptions): String {

    val ca = lexDFHZ(text)
    val superblocks = buildNewPQSuperblocks(ca)
    with(ca) {
        decomposeOBlocks(superblocks)
        rankEBlocks()
        mergeAdjacentKLCBlocks()
        swellBoundaryFBlocks()
        promoteBoundaryKLCBlocks()
        resolveKLCBlocksBySetcPolicy(options.modeSetc)
        check(theseTypesNotPresent("KL"))
        resolveEmbeddedFBlocks()
        resolveHBlocksByArray(lexABHZ(text))
        resolveSolitaryFBlock(options.setWhenJustFnc1)
        dump(10)
        check(theseTypesNotPresent("F"))
        resolveEmbeddedHBlocks()
        dump(11)
        resolveHBlocksByPreference(options.preferSetaOverSetb)
        dump(12)
        check(theseTypesNotPresent("H"))
        check(onlyTheseTypesPresent("ABCZ"))
        dump(14)
    }
    return ca.concatToString()
}

private fun CharArray.dump(n: Int) {
    println("$n: "+this.concatToString())
}