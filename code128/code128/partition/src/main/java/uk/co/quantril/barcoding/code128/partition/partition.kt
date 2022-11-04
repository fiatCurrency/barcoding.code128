package uk.co.quantril.barcoding.code128.partition

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.partition.elementals.*
import uk.co.quantril.barcoding.code128.partition.helpers.buildNewPQSuperblocks
import uk.co.quantril.barcoding.code128.partition.primitives.onlyTheseTypesPresent
import uk.co.quantril.barcoding.code128.partition.primitives.theseTypesNotPresent

public fun partition(text: String, options: EncodingOptions): String {
    val bl = partitionToBlockList(text,options)
    val bl3 = bl.map { (c,x) -> c.toString()+(x.toString().trim()) }
    val sb = StringBuilder()
    var first = true
    bl3.forEach { if (!first) sb.append(" "); sb.append(it); first=false }
    return sb.toString()
}

public fun partitionToBlockList(text: String, options: EncodingOptions): List<Pair<CodeSet,Int>> {
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
        check(theseTypesNotPresent("F"))
        resolveEmbeddedHBlocks()
        resolveHBlocksByPreference(options.preferSetaOverSetb)
        check(theseTypesNotPresent("H"))
        check(onlyTheseTypesPresent("ABCZ"))
    }
    return ca.buildBlockList()
}
