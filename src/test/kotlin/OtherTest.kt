import com.work.Packet
import com.work.convertContentToBinaryList
import com.work.convertHexCharToBinary
import com.work.readFileIntoCharArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File

class OtherTest {

    @Test
    fun `make sure no files throw`() {
        val files = listOf(
            "input.txt",
            "amountInput.txt",
            "lengthInput.txt",
            "literalInput.txt",
            "test.txt",
            "test2.txt",
            "test3.txt",
            "test4.txt"
        )
        for (file in files) {
            assertDoesNotThrow { loadFile(file) }
        }
    }

    private fun loadFile(filename: String) {
        val hexContent = readFileIntoCharArray("task/$filename")
        val binaryContent = convertContentToBinaryList(hexContent)
        Packet(binaryContent)

    }

    @Test
    fun `throw with short input`() {
        val hexContent = "1".toList()
        val binaryContent = convertContentToBinaryList(hexContent)
        assertThrows(IndexOutOfBoundsException::class.java) {
            Packet(binaryContent)
        }
    }

    @Test
    fun `read input file`() {
        val filename = "task/input.txt"
        val expected = File(filename).readText().toList()
        assertEquals(expected, readFileIntoCharArray(filename))
    }

    @Test
    fun `convert list of char(hex) to binary`() {
        val expected = listOf(listOf(0, 0, 0, 0), listOf(1, 1, 1, 1), listOf(1, 0, 0, 0))
        val input = listOf('0', 'F', '8')

        for ((i, hex) in input.withIndex()) {
            val binary = convertHexCharToBinary(hex)
            assertEquals(expected[i].map { it.toByte() }, binary)
        }

    }

    @Test
    fun `throw when trying to convert an invalid byte`() {
        assertThrows(IllegalArgumentException::class.java) { convertHexCharToBinary('G') }
        assertDoesNotThrow { convertHexCharToBinary('F') }
    }

    @Test
    fun `convert a hex string to a list of bits`() {
        val input = "F012"
        val expected = listOf(1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0)
        val result = convertContentToBinaryList(input.toList())
        assertEquals(expected.map { it.toByte() }, result)
    }

    @Test
    fun `convert a bit list to an int`() {
        val input = listOf(listOf(0, 1), listOf(1, 1, 1, 1), listOf(0, 0, 1, 0))
        val expected = listOf(1, 15, 2)
        for ((i, hex) in input.withIndex()) {
            val result = com.work.convertBinaryListToInt(hex.map { it.toByte() })
            assertEquals(expected[i], result)
        }
    }
}