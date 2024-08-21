import com.work.Packet
import com.work.convertContentToBinaryList
import com.work.readFileIntoCharArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PacketAssignmentOneTest {

    @Test
    fun `test for assignment one`() {
        val hexData = readFileIntoCharArray("task/input.txt")
        val binaryData = convertContentToBinaryList(hexData)
        val packet = Packet(binaryData)
        val expected = 843
        val actual = packet.getVersionSum()
        assertEquals(expected, actual)
    }
}