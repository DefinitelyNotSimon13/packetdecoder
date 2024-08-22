package com.work

fun main() {
    val hexContent = readFileIntoCharArray("task/input.txt")
    val binaryContent = convertContentToBinaryList(hexContent)
    println(binaryContent)
    val packet = Packet(binaryContent)

    println("--------------- Assignment 1 ---------------")
    println("Packet Version Sum should be:\t 843")
    println("Actual Value:\t ${packet.getVersionSum()}")
    println("\n\n")

    println("--------------- Assignment 2 ---------------")
    // It freaking overflows Int -.-
    val reduced = packet.calculate()
    println("Packet Reduced Value should be:\t 5390807940351")
    println("Actual Value:\t $reduced")

    println("Loaded Packet!")
}