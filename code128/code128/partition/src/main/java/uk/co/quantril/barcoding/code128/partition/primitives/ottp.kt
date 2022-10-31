package uk.co.quantril.barcoding.code128.partition.primitives

internal fun CharArray.theseTypesNotPresent(prohibited: CharSequence): Boolean {
    this.forEach {
        if (it in prohibited) return false
    }
    return true
}

internal fun CharArray.onlyTheseTypesPresent(allowed: CharSequence): Boolean {
    this.forEach {
        if (it !in allowed) return false
    }
    return true
}
