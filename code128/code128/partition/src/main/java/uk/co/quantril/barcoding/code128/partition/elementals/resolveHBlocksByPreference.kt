package uk.co.quantril.barcoding.code128.partition.elementals

internal fun CharArray.resolveHBlocksByPreference(preferSetA: Boolean) {
    val replacement = if (preferSetA) 'A' else 'B'
    this.forEachIndexed { i,c -> if (c=='H') this[i] = replacement }
}
