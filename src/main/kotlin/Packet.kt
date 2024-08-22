package com.work

data class PacketHeader(val version: Byte, val typeId: Byte, var lengthType: Byte? = null)

class Packet(bits: List<Byte>) {
    val bits: List<Byte>
    private val header: PacketHeader
    private val body: PacketBody

    init {
        val (parsedHeader, bodyBits) = parsePacketHeader(bits)
        this.header = parsedHeader
        val (parsedBody, remainingBits) = parsePacketBody(bodyBits, header)
        this.body = parsedBody
        this.bits = remainingBits
    }

    fun getSize(): Int = when (body) {
        is LiteralBody -> 6 + body.getSize()
        is ParentBody -> 7 + body.getSize()
        else -> error("Unknown body type")
    }

    fun getFlattenedPackets(): List<Packet> =
        listOf(this) + body.getFlattenedChildren()

    fun getVersionSum(): Int =
        getFlattenedPackets().sumOf { it.header.version.toInt() }

    // recursively reduces until only literal values are left
    fun calculate(): Long = body.calculateChildren()
}