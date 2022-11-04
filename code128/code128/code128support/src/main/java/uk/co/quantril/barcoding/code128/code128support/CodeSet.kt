package uk.co.quantril.barcoding.code128.code128support

public enum class CodeSet {
    Seta,
    Setb,
    Setc;

    companion object {
        fun createFrom(c: Char): CodeSet {
            return when(c) {
                'A' -> Seta
                'B' -> Setb
                'C' -> Setc
                else -> throw IllegalArgumentException()
            }
        }
    }

    public override fun toString(): String {
        return when(this) {
            Seta -> "A"
            Setb -> "B"
            Setc -> "C"
        }
    }

}
