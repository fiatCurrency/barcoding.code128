package uk.co.quantril.barcoding.code128.api

import org.junit.Test
import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.EncodingOptions
import uk.co.quantril.barcoding.code128.code128support.SetcPolicy

internal class GenerateGenericKtTest {
    @Test
    internal fun test1() {
        val options = EncodingOptions(
            SetcPolicy.IfShorter,
            preferSetaOverSetb=false,
            setWhenJustFnc1=CodeSet.Setc,
            setWhenEmpty=CodeSet.Setb
        )
        println(generateGeneric("", options))
        println(generateGeneric("56", options))
        println(generateGeneric("145", options))
        println(generateGeneric("`U+000A U+0007 FNC2 FNC4` AB5467DE",options))
        println(generateGeneric("do\n `0x0A` 645`FNC1`4343 Cc", options))

    }

}