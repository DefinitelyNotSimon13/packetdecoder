import com.work.Packet
import com.work.convertContentToBinaryList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PacketCalculationTest {

    @Test
    fun `test literal is correctly 2021`() {
        val sample1 = "D2FE28".toList()
        val samplePacket1 = Packet(convertContentToBinaryList(sample1))
        val result = samplePacket1.reduce()
        assertEquals(2021, result)
    }

    @Test
    fun `test sum of 1 and 2 should return 3`() {
        val sample1 = "C200B40A82".toList()
        val samplePacket1 = Packet(convertContentToBinaryList(sample1))
        val result = samplePacket1.reduce()
        assertEquals(3, result)
    }

    @Test
    fun `test product of 6 and 9 should return 54`() {
        val sample2 = "04005AC33890".toList()
        val samplePacket2 = Packet(convertContentToBinaryList(sample2))
        val result = samplePacket2.reduce()
        assertEquals(54, result)
    }

    @Test
    fun `test minimum of 7, 8, and 9 should return 7`() {
        val sample3 = "880086C3E88112".toList()
        val samplePacket3 = Packet(convertContentToBinaryList(sample3))
        val result = samplePacket3.reduce()
        assertEquals(7, result)
    }

    @Test
    fun `test maximum of 7, 8, and 9 should return 9`() {
        val sample4 = "CE00C43D881120".toList()
        val samplePacket4 = Packet(convertContentToBinaryList(sample4))
        val result = samplePacket4.reduce()
        assertEquals(9, result)
    }

    @Test
    fun `test 5 is less than 15 should return 1`() {
        val sample5 = "D8005AC2A8F0".toList()
        val samplePacket5 = Packet(convertContentToBinaryList(sample5))
        val result = samplePacket5.reduce()
        assertEquals(1, result)
    }

    @Test
    fun `test 5 is not greater than 15 should return 0`() {
        val sample6 = "F600BC2D8F".toList()
        val samplePacket6 = Packet(convertContentToBinaryList(sample6))
        val result = samplePacket6.reduce()
        assertEquals(0, result)
    }

    @Test
    fun `test 1 + 3 = 2 times 2 should return 1`() {
        val sample8 = "9C0141080250320F1802104A08".toList()
        val samplePacket8 = Packet(convertContentToBinaryList(sample8))
        val result = samplePacket8.reduce()
        assertEquals(1, result)
    }
}
