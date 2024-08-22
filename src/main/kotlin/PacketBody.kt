package com.work

// Only expose Interface to outside
// children is exposed for calculation :/
interface PacketBody {
    fun parseContent(bits: List<Byte>): List<Byte>
    fun getSize(): Int
    fun getFlattenedChildren(): List<Packet> = emptyList()
    fun calculateChildren(): Long
}

enum class PacketBodyType {
    LITERAL_BODY, BY_LENGTH_BODY, BY_AMOUNT_BODY
}

// Not so nice with calc!!
fun createClass(bodyType: PacketBodyType, operatorType: Byte? = null): PacketBody {
    return when (bodyType) {
        PacketBodyType.LITERAL_BODY -> LiteralBody()
        PacketBodyType.BY_LENGTH_BODY -> ByLengthBody(operatorType!!)
        PacketBodyType.BY_AMOUNT_BODY -> ByAmountBody(operatorType!!)
    }
}

class LiteralBody : PacketBody {
    private var literalValue: Long? = null
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

    override fun getFlattenedChildren(): List<Packet> = emptyList()

    override fun calculateChildren(): Long = literalValue ?: error("Literal value not set")
}

abstract class ParentBody(operatorType: Byte) : PacketBody, CalculationStrategy by retrieveCalcStrategy(operatorType) {
    val children: MutableList<Packet> = mutableListOf()


    protected fun parseChildPackage(childBits: List<Byte>): List<Byte> {
        return Packet(childBits).also { children.add(it) }.bits
    }

    override fun getSize(): Int = children.sumOf(Packet::getSize)

    override fun getFlattenedChildren(): List<Packet> = children.flatMap(Packet::getFlattenedPackets)

    override fun calculateChildren(): Long = calculate(this)
}


private class ByLengthBody(operatorType: Byte) : ParentBody(operatorType) {
    override fun parseContent(bits: List<Byte>): List<Byte> {
        val lengthSize = 15
        val (lengthBits, remainingBits) = bits.removeStart(lengthSize)
        val length = convertBinaryListToInt(lengthBits)
        val targetLength = remainingBits.size - length

        return parseChildren(remainingBits, targetLength)
    }

    private fun parseChildren(
        remainingBits: List<Byte>,
        targetLength: Int
    ): List<Byte> {
        var remainingBits1 = remainingBits
        while (remainingBits1.size > targetLength) {
            remainingBits1 = parseChildPackage(remainingBits1)
        }
        return remainingBits1
    }
}

private class ByAmountBody(operatorType: Byte) : ParentBody(operatorType) {
    override fun parseContent(bits: List<Byte>): List<Byte> {
        val lengthSize = 11

        val (amountBits, remainingBits) = bits.removeStart(lengthSize)
        val amount = convertBinaryListToInt(amountBits)

        return parseChildren(amount, remainingBits)

    }

    private fun parseChildren(
        amount: Int,
        remainingBits: List<Byte>
    ): List<Byte> {
        var remainingBits1 = remainingBits
        for (i in 1..amount) {
            remainingBits1 = parseChildPackage(remainingBits1)
        }
        return remainingBits1
    }
}