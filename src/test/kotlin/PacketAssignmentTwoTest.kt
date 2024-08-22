import com.work.Packet
import com.work.convertContentToBinaryList
import com.work.readFileIntoCharArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PacketAssignmentTwoTest {
    @Test
    fun `test for assignment two`() {
        val hexData = readFileIntoCharArray("task/input.txt")
        val binaryData = convertContentToBinaryList(hexData)
        val packet = Packet(binaryData)
        val expected = 5390807940351
        val actual = packet.calculate()
        assertEquals(expected, actual)
    }
}