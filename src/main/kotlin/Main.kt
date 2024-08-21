package com.work

fun main() {
    val hexContent = readFileIntoCharArray("task/test2.txt")
    val binaryContent = convertContentToBinaryList(hexContent)
    println(binaryContent)

    val packet = Packet(binaryContent)

    println("Loaded Packet!")
    println(packet.getLength())

    val allPackets = packet.getFlattenedPackets()
    println("Total of ${allPackets.size}")
    for (child in allPackets) {
        println("\tHeader: ${child.header}")
        println("\tBody: ${child.body}")
        if(child.body is LiteralContent) {
            println("\t\tValue: ${(child.body as LiteralContent).literalValue}")
        }
    }
    println("The packages have a version sum of ${packet.getVersionSum()}")


//    val allPackets = packet.getFlattenedPackets()
//    println("Input: $binaryContent")
//    println("All packets:")
//    for (childPacket in allPackets) {
//        println("\tHeader: ${childPacket.header}")
//        println("\tBody: ${childPacket.body}")
//        if (childPacket.body is LiteralContent) {
//            println("\t\tLiteral: ${(childPacket.body as LiteralContent).literalValue}")
//        }
//    }
}

