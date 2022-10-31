package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlock
import uk.co.quantril.barcoding.code128.partition.primitives.blockExtent
import uk.co.quantril.barcoding.code128.partition.primitives.seekNextBlock
import uk.co.quantril.barcoding.code128.partition.primitives.theseTypesNotPresent

internal fun CharArray.rankEBlocks() {
    var index = 0
    while (index < this.size) {
        index = if (this[index] == 'E') {
            val extent = this.blockExtent(index)
            require(extent > 0 && extent % 2 == 0)
            this.overwriteBlock(index,when (extent) { 2->'K'; 4->'L';  else->'C' })
        } else
            this.seekNextBlock(index)
    }
    check(theseTypesNotPresent("E"))
}