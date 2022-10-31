package uk.co.quantril.barcoding.code128.partition.elementals

// Re-map the 'H' blocks into 'A' or 'B' blocks.
// This is done in a stateless manner: the resolution depends only on the code-range of the
//   underlying character, which is passed in as habArray; it does not depend on neighbouring
//   characters.
// If a block is already committed to being something other than 'H', it will remain unchanged.
// 'H' blocks can and will be split up into multiple 'A', 'B', 'H' blocks
internal fun CharArray.resolveHBlocksByArray(habArray: CharArray) {
    require (this.size == habArray.size)
    habArray.forEachIndexed { i,c->
        if (this[i]=='H' && c in "AB")
            this[i] = c
    }
}