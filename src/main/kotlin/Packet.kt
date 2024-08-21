package com.work

import kotlin.system.exitProcess

data class PacketHeader(val version: Int, val typeId: Int)


interface PacketContent {
    fun parsePacketContent()
    fun getBodyLength(): Int
}


class Packet(private var bits: List<Int>) {
    var header: PacketHeader? = null
    var body: PacketContent? = null

    init {
        require(bits.size >= 6) { "Paket has to few bits - ${bits.size}" }
        parsePacketHeader()
        loadPacketContent()
        println("Loaded Packet - $header - ${body!!::class}")
        if (body is LiteralContent) {
            println("Value: ${(body as LiteralContent).literalValue}")
        }
    }

    private fun parsePacketHeader() {
        val (headerBits, remainingBits) = bits.removeStart(6)
        bits = remainingBits

        val versionBits = headerBits.subList(0, 3)
        val idBits = headerBits.subList(3, 6)

        header = PacketHeader(
            convertBinaryListToInt(versionBits),
            convertBinaryListToInt(idBits)
        )
    }

    private fun loadPacketContent() {
        var operator: Int? = null
        if (header?.typeId != 4) {
            val (operatorType, remainingBits) = bits.removeStart(1)
            bits = remainingBits
            operator = operatorType[0]
        }
        when (operator) {
            null -> body = LiteralContent(bits)
            0 -> body = ByLengthContent(bits)
            1 -> body = ByAmountContent(bits)
        }
        if (body == null) throw Exception("Body is empty")
        body?.parsePacketContent()!!
    }

    fun getLength(): Int {
        if (header == null) return 0
        when (body) {
            null -> return 6
            is LiteralContent -> return 6 + body!!.getBodyLength()
            is ByLengthContent -> return 6 + body!!.getBodyLength()
            is ByAmountContent -> return 6 + body!!.getBodyLength()
        }
        throw IllegalArgumentException("Unsupported body")
    }

    fun getFlattenedPackets(): List<Packet> {
        val list = mutableListOf(this)
        if (body is ParentPacket) list += (body as ParentPacket).getFlattenedChildren()
        return list
    }

    fun getVersionSum(): Int {
        return getFlattenedPackets().fold(0) { sum, packet -> sum + packet.header!!.version }
    }
}

class LiteralContent(private val bits: List<Int>) : PacketContent {
    init {
        require(bits.size >= 5) { "Literal needs at least 5 bits"}
    }

    var literalValue: Int? = null
    private var contentLength: Int = 0


    override fun parsePacketContent() {
        val groups = bits.chunked(5)
        val valueBits: MutableList<Int> = mutableListOf()
        for (group in groups) {
            valueBits += group.slice(1 until group.size)
            contentLength += 5
            if (group[0] == 0) {
                break
            }
        }
        literalValue = convertBinaryListToInt(valueBits)
    }

    override  fun getBodyLength(): Int {
        return contentLength
    }
}

abstract class ParentPacket(val bits: List<Int>) : PacketContent {
    protected val content: MutableList<Packet> = mutableListOf()

    protected fun parseNextPackage(remainingBits: List<Int>): List<Int> {
        val nextPacket = Packet(remainingBits)
        content += nextPacket
        return remainingBits.removeStart(nextPacket.getLength()).second
    }

    override fun getBodyLength(): Int {
        return content.fold(0) { acc, packet -> acc + packet.getLength() }
    }

     fun getFlattenedChildren(): List<Packet> {
        val list: MutableList<Packet> = mutableListOf()
        for (packet in content) {
            list += packet.getFlattenedPackets()
        }
         return list
    }

}

class ByLengthContent(bits: List<Int>) : ParentPacket(bits) {
    var length: Int = 0
    init {
        require(bits.size >= 15) { "ByLengthPackets needs at least 15 bits tried with $bits"}
    }
    override fun parsePacketContent() {
        var (lengthBits, remainingBits) = bits.removeStart(15)
        length = convertBinaryListToInt(lengthBits)
        println("LengthBits: $lengthBits")
        println("ParsingPacket with length $length while ${remainingBits.size} bits remain")
        var (toBeUsedBits, otherBits) = remainingBits.removeStart(length)

        while (toBeUsedBits.size > 0) {
            toBeUsedBits = parseNextPackage(toBeUsedBits)
            println("Now containing ${content.size} packets - ${toBeUsedBits.size} bits remaining")
//            } catch (e: IllegalArgumentException) {
//                println("Parsing failed")
//                println("Desired length: $length")
//                println("Current BodyLength: ${this.getBodyLength()}")
//                println(e.message)
//                throw IllegalStateException("Parsing failed - go to parent")
//            }
        }
        println("Total of ${content.map { it.getLength() }.sum()} length")
    }
}

class ByAmountContent(bits: List<Int>) : ParentPacket(bits) {
    var amount = 0
    init {
        require(bits.size >= 11) { "ByAmountPackets needs at least 11 bits tried with $bits"}
    }
    override fun parsePacketContent() {
        var (amountBits, remainingBits) = bits.removeStart(11)
        amount = convertBinaryListToInt(amountBits)
        for (i in 1 .. amount) {
            remainingBits = parseNextPackage(remainingBits)
        }
    }
}