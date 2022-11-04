package uk.co.quantril.barcoding.code128.encodation

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.code128support.Fnc

internal fun encode(code: CodeSet, text: CharSequence): Encodation {
    return Encodation(
        code,
        when(code) {
            CodeSet.Seta -> loaderAB(text,true)
            CodeSet.Setb -> loaderAB(text,false)
            CodeSet.Setc -> loaderC(text)
        }
    )
}

private fun loaderAB(text: CharSequence, useA: Boolean) = sequence {
    text.forEach {
        when(val x = it.code) {
            // Middle region 0x20..0x5F
            // Encodes as 0x00..0x3F: Encoded equally in both Seta and Setb
            in 0x20..0x5F -> {
                yield(x - 0x20)
            }
            // Control Codes 0x00..0x1F
            // Encodes as 0x40..0x5F in Seta. Use Shift if we are in Setb
            in 0x00..0x1F -> {  // Control codes, Seta. Use Shift if we are in Setb
                if (!useA) yield(shiftAB)
                yield(x + 0x40)
            }
            // Upper region 0x60..0x7F. Mainly lower-case letters
            // Encodes as 0x40..0x5F in Setb. Use Shift if we are in Setb
            in 0x60..0x7F -> {
                if (useA) yield(shiftAB)
                yield(x - 0x20)   // Render as 0x40..0x5F
            }
            // Function Codes 1 to 3, which we represent in the Unicode Private Use Area.
            // These all render to the same code-point in all the Setx in which they
            //  are supported
            Fnc.fnc1Code -> yield(function1ABC)
            Fnc.fnc2Code -> yield(function2AB)
            Fnc.fnc3Code -> yield(function3AB)
            // Function Code 4. Again, this is represented in the Unicode PUA
            // It has different rerepresentations between Seta and Setb as it alternates
            //   a code-point with codeCodeA and codeCodeB.
            Fnc.fnc4Code -> yield(if (useA) function4A else function4B)
            //
            // We use require() here as the incoming characters are assumed already to
            //   have been validated. We use require(), rather than check(), as such
            //   a breach is a failure to meet the input contract, not a failure of
            //   internal logic.
            else -> require(false) { "Invalid character: Code $x" }
        }
    }
}

private fun loaderC(text: CharSequence) = sequence {
    val iterator = text.iterator()
    while (iterator.hasNext()) {
        when (val c1 = iterator.next()) {
            Fnc.fnc1 -> yield(function1ABC)
            in '0'..'9' -> {
                require(iterator.hasNext())
                val c2 = iterator.next()
                require(c2 in '0'..'9')
                val number = 10*(c1-'0') + (c2-'0')
                yield(number)
            }
            else -> require(false)
        }
    }
}

