package com.work

import java.io.File
import java.security.InvalidParameterException

fun readFileIntoCharArray(filename: String): List<Char> = File(filename).readText().toList()

fun convertHexCharToBinary(input: Char): List<Byte> {
    val hexValue = input.digitToIntOrNull(16) ?: throw InvalidParameterException("$input is invalid")
    return hexValue.toString(2).padStart(4, '0').map { it.digitToInt().toByte() }
}

fun convertContentToBinaryList(input: List<Char>): List<Byte> = input.flatMap { convertHexCharToBinary(it) }

// Shift accumulator to left by 1 for each bit, then add current bit with bitwise or
// for example - (1, 0, 1)
// 0 shifted left by 1 = 0      |    0 or 1 = 1
// 1 shifted left by 1 = 10     |    10 or 0 = 10
// 10 shifted left by 1 = 100   |    100 or 1 = 101
fun convertBinaryListToInt(input: List<Byte>): Int =
    input.fold(0) { accumulator, bit -> (accumulator shl 1) or bit.toInt() }

fun <T> List<T>.removeStart(toIndex: Int): Pair<List<T>, List<T>> =
    this.subList(0, toIndex) to this.drop(toIndex)
