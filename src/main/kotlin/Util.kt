package com.work

import java.io.File
import java.security.InvalidParameterException

fun readFileIntoCharArray(filename: String): List<Char> = File(filename).readText().toList()

fun convertHexCharToBinary(input: Char): List<Int> {
    val hexValue = input.digitToIntOrNull(16) ?: throw InvalidParameterException("$input is invalid")
    return hexValue.toString(2).padStart(4, '0').map { it.digitToInt() }
}

fun convertContentToBinaryList(input: List<Char>): List<Int> = input.map { convertHexCharToBinary(it) }.flatten()

// Shift accumulator to left by 1 for each bit, then add current bit with bitwise or
fun convertBinaryListToInt(input: List<Int>): Int = input.fold(0) { accumulator, bit -> (accumulator shl 1) or bit }

fun<T> List<T>.removeStart(toIndex: Int): Pair<List<T>, List<T>> {
    val firstThree = this.subList(0, toIndex)
    val rest = this.drop(toIndex)
    return Pair(firstThree, rest)
}
