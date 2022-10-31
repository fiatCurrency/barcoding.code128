package uk.co.quantril.barcoding.code128.unescape.support

internal data class Reel(
    val output: StringBuilder,
    val input: CharSequence,
    var index: Int
) {
    init {
        check(isSane())
    }

    private val head: Char get() {
        check(inputRemains())
        return input[index]
    }

        // input.length is the EOF condition, which we allow.
    private fun isSane(): Boolean {
        return(index in 0..input.length)
    }

    constructor(cs: CharSequence) : this(StringBuilder(),cs,0)

    public fun skip(ch: Char) {
        while(atHead(ch)) consumeHead()
    }

    public fun atHead(ch: Char): Boolean {
        return (inputRemains() && head==ch)
    }

    public fun consumeHead(): Char {
        check(inputRemains())
        return input[index++]
    }

    public fun inputExhausted(): Boolean {
        return !inputRemains()
    }

    public fun advance(delta: Int): Int {
        require(delta>=0)
        require(inputRemainingIsAtLeast(delta))
        index += delta
        return index
    }

    public fun countMatching(fn: (ch:Char)->Boolean): Int {
        var ix = index
        while ( ix < input.length && fn(input[ix]) )
            ++ix
        return ix - index
    }

    public fun inputRemains(): Boolean {
        return this.index < this.input.length
    }

    public fun inputRemainingIsAtLeast(n: Int): Boolean {
        return (this.input.length - this.index) >= n
    }



}
