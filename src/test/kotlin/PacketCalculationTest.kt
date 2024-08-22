import com.work.Packet
import com.work.convertContentToBinaryList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PacketCalculationTest {

    private fun loadPacket(data: String): Packet {
        val hexContent = data.toList()
        val binaryContent = convertContentToBinaryList(hexContent)
        return Packet(binaryContent)
    }

    @Test
    fun `test literal is correctly 2021`() {
        val packet = loadPacket("D2FE28")
        val result = packet.calculate()
        assertEquals(2021, result)
    }

    @Test
    fun `test sum of 1 and 2 should return 3`() {
        val packet = loadPacket("C200B40A82")
        val result = packet.calculate()
        assertEquals(3, result)
    }

    @Test
    fun `test product of 6 and 9 should return 54`() {
        val packet = loadPacket("04005AC33890")
        val result = packet.calculate()
        assertEquals(54, result)
    }

    @Test
    fun `test minimum of 7, 8, and 9 should return 7`() {
        val packet = loadPacket("880086C3E88112")
        val result = packet.calculate()
        assertEquals(7, result)
    }

    @Test
    fun `test maximum of 7, 8, and 9 should return 9`() {
        val packet = loadPacket("CE00C43D881120")
        val result = packet.calculate()
        assertEquals(9, result)
    }

    @Test
    fun `test 5 is less than 15 should return 1`() {
        val packet = loadPacket("D8005AC2A8F0")
        val result = packet.calculate()
        assertEquals(1, result)
    }

    @Test
    fun `test 5 is not greater than 15 should return 0`() {
        val packet = loadPacket("F600BC2D8F")
        val result = packet.calculate()
        assertEquals(0, result)
    }

    @Test
    fun `test 1 + 3 = 2 times 2 should return 1`() {
        val packet = loadPacket("9C0141080250320F1802104A08")
        val result = packet.calculate()
        assertEquals(1, result)
    }

    @Test
    fun `comparison with 3 children should throw`(){
        val packet = loadPacket("FA00D40C823060")
        assertThrows(IllegalArgumentException::class.java) {
            packet.calculate()
        }
    }

    @Test
    fun `1 times 2 times 3 should return 6`(){
        val packet = loadPacket("E600D40C823060")
        val result = packet.calculate()
        assertEquals(6, result)
    }

    @Test
    fun `1 + 2 + 4 should return 7`(){
        val packet = loadPacket("E200D40C823080")
        val result = packet.calculate()
        assertEquals(7, result)
    }

    @Test
    fun `min of 1 2 3 should be 1`(){
        val packet = loadPacket("EA00D40C823060")
        val result = packet.calculate()
        assertEquals(1, result)
    }

    @Test
    fun `max of 1 2 3 should be 3`(){
        val packet = loadPacket("EE00D40C823060")
        val result = packet.calculate()
        assertEquals(3, result)
    }

    @Test
    fun `10 less then 20 should be 1`(){
        val packet = loadPacket("38006F45291200")
        val result = packet.calculate()
        assertEquals(1, result)
    }

    @Test
    fun `10 more then 20 should be 0`(){
        val packet = loadPacket("30006F45291200")
        val result = packet.calculate()
        assertEquals(0, result)
    }

    @Test
    fun `10 equal to 20 should be 0`(){
        val packet = loadPacket("3C006F45291200")
        val result = packet.calculate()
        assertEquals(0, result)
    }

    @Test
    fun `5 equal to 5 should be 1`(){
        val packet = loadPacket("9C005AC2F850")
        val result = packet.calculate()
        assertEquals(1, result)
    }
}