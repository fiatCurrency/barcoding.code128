package uk.co.quantril.barcoding.code128.partition.primitives

public fun CharArray.seekNextBlock(start: Int): Int {
    require( (start >= -1) && (start <= this.size) )
    if (start == this.size) throw IndexOutOfBoundsException()
    var index = start + 1
    if (index > 0) {
        while (index < this.size && (this[index - 1] == this[index])) {
            index++
        }
    }
    return index
}

public fun CharArray.seekNextBlockSaturating(start: Int): Int {
    require( (start>=-1) && (start<=this.size) )
    return if ( start == this.size ) this.size else seekNextBlock(start)
}

public fun CharArray.rseekBlockStart(rstart: Int): Int {
    require(rstart >= -1 && rstart <= this.size)
    if (rstart == -1) throw IndexOutOfBoundsException()
    var index = rstart
    check(index >= 0 && index <= this.size)
    if (index < this.size) {
        while (index > 0 && this[index] == this[index - 1])
            index--
    }
    check(index >= 0 && index <= this.size)
    return index
}

public fun CharArray.rseekPreviousBlockEnd(rstart: Int): Int {
    val blockStart = this.rseekBlockStart(rstart)
    check(blockStart >= 0 && blockStart <= this.size)
    return blockStart - 1
}

// Return the first index containing the passed block type, starting from start, and
//   always seeking forward.
// The index found will be the first of the containing block, except:
//    if the passed index is already in the middle of a block of the required type, it will
//      be returned unchanged;
//    if there is no such block type (when seeking forwards), the limiting value will be
//      returned. This is the size of the array
public fun CharArray.seekBlock(start: Int, type: Char): Int {
    require(start >= -1 && start <= this.size)
    var index = start
    while (index < this.size && type != this[index]) {
        index++
    }
    return index
}
