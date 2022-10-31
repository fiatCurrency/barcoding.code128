package uk.co.quantril.barcoding.code128.code128support

// These are "private use area" Unicode code points, for use when we have to
//   handle function codes in what are nominally ASCII strings.
public object Fnc {
    public const val fnc1Code: Int = 0xE0F1
    public const val fnc2Code: Int = 0xE0F2
    public const val fnc3Code: Int = 0xE0F3
    public const val fnc4Code: Int = 0xE0F4

    public const val fnc1: Char = fnc1Code.toChar()
    public const val fnc2: Char = fnc2Code.toChar()
    public const val fnc3: Char = fnc3Code.toChar()
    public const val fnc4: Char = fnc4Code.toChar()
}







