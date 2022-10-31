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
        //val s = generateGeneric("Freddo\n `0x0A` 645`FNC1`4343 Cc", options)
        val s = generateGeneric("do\n `0x0A` 645`FNC1`4343 Cc", options)
        println(s)
    }

}