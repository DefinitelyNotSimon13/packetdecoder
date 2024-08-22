package com.work

fun parsePacketHeader(bits: List<Byte>): Pair<PacketHeader, List<Byte>> {
    val header = PacketHeader(
        convertBinaryListToInt(bits.subList(0, 3)).toByte(),
        convertBinaryListToInt(bits.subList(3, 6)).toByte()
    )

    var remainingBits = bits.drop(6)
    if (header.typeId.toInt() != 4) {
        header.lengthType = remainingBits.firstOrNull()
        remainingBits = remainingBits.drop(1)
    }
    return header to remainingBits
}

fun parsePacketBody(bits: List<Byte>, header: PacketHeader): Pair<PacketBody, List<Byte>> {
    val parsedBody: PacketBody = when (header.lengthType?.toInt()) {
        null -> createClass(PacketBodyType.LITERAL_BODY)
        0 -> createClass(PacketBodyType.BY_LENGTH_BODY, header.typeId)
        1 -> createClass(PacketBodyType.BY_AMOUNT_BODY, header.typeId)
        else -> throw IllegalArgumentException("Unknown lengthType: ${header.lengthType}")
    }
    val remainingBits = parsedBody.parseContent(bits)
    return parsedBody to remainingBits
}
