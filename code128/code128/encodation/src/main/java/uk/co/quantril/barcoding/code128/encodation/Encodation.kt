package uk.co.quantril.barcoding.code128.encodation

import uk.co.quantril.barcoding.code128.code128support.CodeSet
import java.util.*

public class Encodation(code: CodeSet, loader: Sequence<Int>) {
    // A newly-constructed Encodation object will represent the encoding of
    //   data in a /single/ CodeSet. Thus, headCode and tailCode will
    //   get initialised to the same value
    private var headCode: CodeSet
    private var tailCode: CodeSet

    // list contains the numeric code-points corresponding to what is being
    //   encoded. These can only be those which correspond to actual characters (or
    //   function codes).
    //
    // In particular, the three Start Codes are not allowed (nor anything
    //   numerically above Start Code C, 105)
    //
    // The Code change code-points are allowed but only if they are for the
    //   same CodeSet as we are currently "in"; in this case, they won't be treated
    //   as Code change codes, because each such has a specfic function when we are
    //   already "in" that CodeSet:
    //      CodeA in Seta becomes fnc4
    //      CodeB in Setb becomes fnc4
    //      CodeC in Setc becomes the digit-pair "99"
    //
    // Code-point 98 /is/ allowed, either as Shift, or as digit-pair 98.
    //
    // It /is/ allowed to use the Code change code-points for actually changing
    //   CodeSet, but not during construction, as a newly-constructed Encodation
    //   is assumed to represent a single CodeSet (temporary Shift operations between
    //   Seta and Setb notwithstanding)
    //
    // The actual Start code is not stored directly. It is made manifest at the point
    //   of output.
    // Similarly, the checksum and Stop code are not handled by the Encodation class
    private var list: MutableList<Int> = LinkedList<Int>()

    private val isEmpty: Boolean
        get() {
            return list.isEmpty()
        }

    init {
        headCode = code
        tailCode = code
        val codeCode = codeCodeForCodeSet(code)
        loader.forEach {
            require(isAllowableForCodeCode(codeCode,it))
            list.add(it)
        }
    }

    public fun output() = sequence<Int> {
        yield(startCodeForCodeSet(headCode))
        list.forEach { yield(it) }
    }

    // Function to append the code-points of another Encodation enc2 into this one.
    // The Encodation enc2 is left unchanged.
    // The code-points from enc2 are appended in order to the list of code-points
    //   in this Encodation.
    // A Code change code-point is inserted if the incoming Encodation is of a
    //   different CodeSet to this one. This will mean that tailCode may be different
    //   from headCode.
    public fun append(enc2: Encodation) {
        if (this.isEmpty && enc2.isEmpty) {
            this.set(enc2)
        } else if (enc2.isEmpty) {
            // Do nothing
        } else if (this.isEmpty) {
            this.set(enc2)
        } else {
            if (this.tailCode != enc2.headCode)
                this.list.add(codeCodeForCodeSet(enc2.headCode))
            enc2.list.forEach { this.list.add(it) }
            this.tailCode = enc2.tailCode
        }
    }

    public fun set(enc2: Encodation) {
        this.headCode = enc2.headCode
        this.tailCode = enc2.tailCode
        this.list.clear()
        enc2.list.forEach { this.list.add(it) }
    }

}

private fun codeCodeForCodeSet(cs: CodeSet): Int {
    return when(cs) {
        CodeSet.Seta -> codeCodeA
        CodeSet.Setb -> codeCodeB
        CodeSet.Setc -> codeCodeC
    }
}

private fun startCodeForCodeSet(cs: CodeSet): Int {
    return when(cs) {
        CodeSet.Seta -> startA
        CodeSet.Setb -> startB
        CodeSet.Setc -> startC
    }
}


const val startA = 103
const val startB = 104
const val startC = 105
const val codeCodeA = 101
const val codeCodeB = 100
const val codeCodeC = 99
const val shiftAB = 98
const val function1ABC = 102
const val function2AB = 97
const val function3AB = 96
const val function4A = 101
const val function4B = 100

val startCodes = listOf(startA, startB, startC)
val codeCodes = listOf(codeCodeA, codeCodeB, codeCodeC)

private fun isAllowableForCodeCode(codeCode: Int, test: Int): Boolean {
    if (test==codeCode) return true
    if (test in 0..98) return true
    if (test == function1ABC) return true
    return false
}