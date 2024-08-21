package com.work

fun main() {
    val hexContent = readFileIntoCharArray("task/input.txt")
    val binaryContent = convertContentToBinaryList(hexContent)

    val packet = Packet(binaryContent)

    println("Loaded Packet!")

    val length = packet.getLength()
    println("Length is $length")
//    println("All packets:")
//    for (childPacket in allPackets) {
//        println("\tHeader: ${childPacket.header}")
//        println("\tBody: ${childPacket.body}")
//    }
}
