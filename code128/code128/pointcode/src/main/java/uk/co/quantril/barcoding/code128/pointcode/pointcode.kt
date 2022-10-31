package uk.co.quantril.barcoding.code128.pointcode

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import uk.co.quantril.barcoding.code128.pointcode.elementals.*
import uk.co.quantril.barcoding.code128.pointcode.elementals.applyCodeChanges
import uk.co.quantril.barcoding.code128.pointcode.elementals.encodeForSetc
import uk.co.quantril.barcoding.code128.pointcode.elementals.weave
import uk.co.quantril.barcoding.code128.pointcode.helpers.render1
import uk.co.quantril.barcoding.code128.pointcode.helpers.render2

// Encode the passed text (nominally ASCII 7-bit, but includes Code128 Fnc codes) into
//   a List of code points, (Int 0..107), according to the CodeSet as passed in the companion
//   same-sized parameter codeSets
// Code128 uses codes 0..95 to represent "real" ASCII characters (Seta and Setb), 0..99 to
//   represent compressed digit pairs (Setc). Interspersed between these are additional codes
//   96..107 corresponding to the changes of code. Eventually, these will correspond to "real"
//   barcode character patterns on a one-to-one mapping.
// The encoding of the characters changes from "ASCII+function characters" to code points as
//   the encoding progresses.

public fun pointcode(text: String, codeSets: String, defaultStart: CodeSet): List<Int> {
    val s1 = weave(codeSets.asSequence(), text.asSequence())    // All sets in ASCIIF
    val s2 = s1.performCpMappingSeta()
    val s3 = s2.performCpMappingSetb()
    val s4 = s3.performCpMappingSetc()
    val s5 = s4.applyCodeChanges()
        // Now, we are working with a single sequence of integers, the
        //   pairing of each with their CodeSet now having been discharged.
    val s6 = s5.ensureStartCode(defaultStart)               // Special: if text is empty!
        // TODO: Handle SHIFT codes
        // TODO: Compute checksum
        // TODO: Add Stop Code
        // TODO: Add Quiet Zones
    return s6.toList()
}