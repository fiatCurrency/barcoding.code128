package uk.co.quantril.barcoding.code128.partition.elementals

import uk.co.quantril.barcoding.code128.code128support.Fnc

// Map a string into an array denoting whether its individual characters need to be
//   represented in Seta ('A') or Setb ('B').
// Setc is not considered.
// Every /valid/ character can be represented in at least one of the two Sets
// If a character can be represented in either Set, it is mapped as 'H'
// Invalid characters map as 'Z'
internal fun lexABHZ(string: String): CharArray {
    return CharArray(string.length) {
        i -> when (string[i].code) {
            in mustBeSetA -> 'A'
            in mustBeSetB -> 'B'
            in canBeEitherSet -> 'H'
            in functionCharacters -> 'H'
            else -> 'Z'
        }
    }
}

private val mustBeSetA = 0x00..0x1F
private val mustBeSetB = 0x60..0x7F
private val canBeEitherSet = 0x20..0x5F
private val functionCharacters = arrayOf(Fnc.fnc1Code, Fnc.fnc2Code, Fnc.fnc3Code, Fnc.fnc4Code)





