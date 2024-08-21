package com.work

data class PacketHeader(val version: Int, val typeId: Int)


interface PacketContent {
    fun parsePacketContent(bits: List<Int>): Int
}

class Packet(var bits: List<Int>) {
    var header: PacketHeader? = null
    var body: PacketContent? = null
    init {
        println("Initializing")
    }

    fun loadPacket(): List<Int> {
        parsePacketHeader()
        loadPacketContent()
        return bits
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
        when(header?.typeId) {
            0 -> body = ByLengthContent()
            1 -> body = ByAmountContent()
            4 -> body = LiteralContent()
        }
        val contentSize = body?.parsePacketContent(bits)
        val (_, remainingBits) = bits.removeStart(contentSize!!)
        bits = remainingBits
        println("Remaining: ${bits.size}")
        println(bits)
    }
}

class LiteralContent() : PacketContent {
    var literalValue: Int? = null
        private set
    override fun parsePacketContent(bits: List<Int>): Int {
        println(bits)
        val groups = bits.chunked(5)
        val valueBits: MutableList<Int> = mutableListOf()
        var usedGroups: Int = 0
        for (group in groups) {
            usedGroups++
            println(group)
            valueBits += group.slice(1..4)

            if (group[0] == 0) {
                break
            }
        }
        literalValue = convertBinaryListToInt(valueBits)
        return usedGroups * 5
    }
}

class ByLengthContent() : PacketContent {
    override fun parsePacketContent(bits: List<Int>): Int {
        TODO("Not yet implemented")
    }
}

class ByAmountContent() : PacketContent {
    override fun parsePacketContent(bits: List<Int>): Int {
        TODO("Not yet implemented")
    }
}