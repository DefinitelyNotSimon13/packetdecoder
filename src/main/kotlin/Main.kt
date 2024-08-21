package com.work

import java.io.File
import java.security.InvalidParameterException

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val hexContent = readFileIntoCharArray("task/literalInput.txt")
    val binaryContent = convertContentToBinaryList(hexContent)

    val packet = Packet(binaryContent)
    packet.loadPacket()
    println(packet.header)
    if(packet.body is LiteralContent) {
        println((packet.body as LiteralContent).literalValue)
    }
}

fun readFileIntoCharArray(filename: String): List<Char> = File(filename).readText().toList()

fun convertHexCharToBinary(input: Char): List<Int> {
    val hexValue = input.digitToIntOrNull(16) ?: throw InvalidParameterException("$input is invalid")
    return hexValue.toString(2).padStart(4, '0').map { it.digitToInt() }
}

fun convertContentToBinaryList(input: List<Char>): List<Int> = input.map { convertHexCharToBinary(it) }.flatten()

// Shift accumulator to left by 1 for each bit, then add current bit with bitwise or
fun convertBinaryListToInt(input: List<Int>): Int = input.fold(0) { accumulator, bit -> (accumulator shl 1) or bit }

fun createPacketHeader(input: List<Int>): PacketHeader {
    val encodedVersion = input.subList(0, 3)
    val encodedId = input.subList(3, 6)
    return PacketHeader(
        convertBinaryListToInt(encodedVersion),
        convertBinaryListToInt(encodedId)
    )
}

fun<T> List<T>.removeStart(toIndex: Int): Pair<List<T>, List<T>> {
    val firstThree = this.subList(0, toIndex)
    val rest = this.drop(toIndex)
    return Pair(firstThree, rest)
}
