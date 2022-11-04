package uk.co.quantril.barcoding.code128.encodation

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.EncodingOptions

fun encodeFromBlockList(bl: List<Pair<CodeSet,Int>>, text: CharSequence, options: EncodingOptions): Encodation {
    val output = encode(options.setWhenEmpty,"")
    var index = 0
    val textLength = text.length
    bl.forEach { (codeSet,extent) ->
        require(index+extent <= textLength)
        output.append(encode(codeSet,text.subSequence(index,index+extent)))
        index += extent
    }
    return output
}