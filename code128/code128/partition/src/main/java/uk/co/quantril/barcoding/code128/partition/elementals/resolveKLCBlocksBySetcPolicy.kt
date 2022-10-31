package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.code128support.SetcPolicy
import uk.co.quantril.barcoding.code128.partition.primitives.overwriteBlockIfNonZero
import uk.co.quantril.barcoding.code128.partition.primitives.zeroBlock

internal fun CharArray.resolveKLCBlocksBySetcPolicy(policy: SetcPolicy) {

    fun mapper(b: Char): Char {
        return when (policy) {
            SetcPolicy.Never -> {
                if (b in "KLC") 'H' else zeroBlock
            }
            SetcPolicy.IfPossible, SetcPolicy.Always -> {
                if (b in "KLC") 'C' else zeroBlock
            }
            SetcPolicy.IfNoLonger -> {
                if (b in "LC") 'C' else if (b == 'K') 'H' else zeroBlock
            }
            SetcPolicy.IfShorter -> {
                if (b in "KL") 'H' else if (b == 'C') 'C' else zeroBlock
            }
        }
    }

    var index = 0
    while (index < this.size) {
        index = this.overwriteBlockIfNonZero(index, mapper(this[index]))
    }

    forEachIndexed { ix, ch -> if (ch in "KL") this[ix] = 'H' }
}
