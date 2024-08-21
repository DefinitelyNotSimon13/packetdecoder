package com.work

interface PacketBody {
    fun parseContent(bits: List<Byte>): List<Byte>
    fun getSize(): Int
}

class LiteralBody : PacketBody {
    var literalValue: Long? = null
        private set
    private var contentLength: Int = 0

    override fun parseContent(bits: List<Byte>): List<Byte> {
        // Iterators?!
        val groups = bits.chunked(5)
        val valueBits: MutableList<Byte> = mutableListOf()
        for (group in groups) {
            valueBits += group.slice(1 until 5)
            contentLength += 5
            if (group[0].toInt() == 0) {
                break
            }
        }
        literalValue = convertBinaryListToInt(valueBits).toLong()
        return bits.drop(contentLength)
    }

    override fun getSize(): Int = contentLength
}

abstract class ParentBody(private val calculator: CalculationStrategy) : PacketBody {
    val children: MutableList<Packet> = mutableListOf()

    protected fun parseChildPackage(childBits: List<Byte>): List<Byte> {
        val nextPacket = Packet(childBits)
        children.add(nextPacket)
        val returnBits = nextPacket.bits
        return returnBits
    }

    override fun getSize(): Int = children.fold(0) { sum, packet -> sum + packet.getSize() }

    fun getFlattenedChildren(): List<Packet> = children.flatMap { it.getFlattenedPackets() }

    fun calculate(): Long = calculator.calculate(this)
}

class ByLengthBody(calc: CalculationStrategy) : ParentBody(calc) {
    private val lengthSize = 15
    private var length: Int = 0

    override fun parseContent(bits: List<Byte>): List<Byte> {
        var (lengthBits, remainingBits) = bits.removeStart(lengthSize)
        length = convertBinaryListToInt(lengthBits)
        val targetLength = remainingBits.size - length

        while (remainingBits.size > targetLength) {
            remainingBits = parseChildPackage(remainingBits)
        }
        return remainingBits
    }
}

class ByAmountBody(calc: CalculationStrategy) : ParentBody(calc) {
    private val lengthSize = 11
    private var amount = 0

    override fun parseContent(bits: List<Byte>): List<Byte> {
        var (amountBits, remainingBits) = bits.removeStart(lengthSize)
        amount = convertBinaryListToInt(amountBits)
        for (i in 1..amount) {
            remainingBits = parseChildPackage(remainingBits)
        }
        return remainingBits
    }
}