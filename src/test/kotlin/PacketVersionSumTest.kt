import com.work.Packet
import com.work.convertContentToBinaryList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PacketVersionSumTest {

    @Test
    fun `version sum should be 16`() {
        val sample1 = "8A004A801A8002F478".toList()
        val samplePacket1 = Packet(convertContentToBinaryList(sample1))
        val result = samplePacket1.getVersionSum()
        assertEquals(16, result)
    }

    @Test
    fun `version sum should be 12`() {
        val sample2 = "620080001611562C8802118E34".toList()
        val samplePacket2 = Packet(convertContentToBinaryList(sample2))
        val result = samplePacket2.getVersionSum()
        assertEquals(12, result)
    }

    @Test
    fun `version sum should be 23`() {
        val sample3 = "C0015000016115A2E0802F182340".toList()
        val samplePacket3 = Packet(convertContentToBinaryList(sample3))
        val result = samplePacket3.getVersionSum()
        assertEquals(23, result)
    }

    @Test
    fun `version sum should be 31`() {
        val sample4 = "A0016C880162017C3686B18A3D4780".toList()
        val samplePacket4 = Packet(convertContentToBinaryList(sample4))
        val result = samplePacket4.getVersionSum()
        assertEquals(31, result)
    }
}
