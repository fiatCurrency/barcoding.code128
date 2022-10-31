package uk.co.quantril.barcoding.code128.partition.elementals

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import uk.co.quantril.barcoding.code128.partition.elementals.resolveEmbeddedHBlocks

internal class ResolveEmbeddedHBlocksKtTest {
    @Test
    internal fun test1() {
        val ca = "BBAAAHHCCCCCCCHHB".toCharArray()
        val actual = ca.clone()
        val expected = "BBAAAAACCCCCCCBBB".toCharArray()
        actual.resolveEmbeddedHBlocks()
        assertThat(actual,equalTo(expected))
    }
}