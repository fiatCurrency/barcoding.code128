package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.partition.helpers.blockTypeFromCodeSet
import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlock
import uk.co.quantril.barcoding.code128.partition.primitives.inBlockP
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock

internal fun CharArray.resolveSolitaryFBlock(loneF: CodeSet) {
    if (inBlockP(0, "F") && seekNextBlock(0) == size)
        overwriteBlock(0, blockTypeFromCodeSet(loneF))
}