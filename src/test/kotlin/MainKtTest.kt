import com.work.convertHexCharToBinary
import com.work.readFileIntoCharArray
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File

class MainKtTest {

    @Test
    fun readFile() {
        val filename = "task/input.txt"
        val expected = File(filename).readText().toList()
        assertEquals(expected, readFileIntoCharArray(filename))
    }

    @Test
    fun convertHexToBinary() {
        val expected = listOf(listOf(0,0,0,0), listOf(1,1,1,1), listOf(1,0,0,0))
        val input = listOf('0', 'F', '8')

        for ((i, hex) in input.withIndex()) {
            val binary = convertHexCharToBinary(hex)
            assertEquals(expected[i], binary)
        }

    }

    @Test
    fun convertInvalidCharToBinary() {
        assertThrows(IllegalArgumentException::class.java) { convertHexCharToBinary('G') }
        assertDoesNotThrow { convertHexCharToBinary('F') }
    }

    @Test
    fun convertContentToBinaryList() {
        val input = "F012"
        val expected = listOf(1,1,1,1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0)
        val result = com.work.convertContentToBinaryList(input.toList())
        assertEquals(expected, result)
    }

    @Test
    fun convertBinaryListToInt() {
        val input = listOf(listOf(0, 1), listOf(1,1,1,1), listOf(0,0,1,0))
        val expected = listOf(1, 15, 2)
        for ((i, hex) in input.withIndex()) {
            val result = com.work.convertBinaryListToInt(hex)
            assertEquals(expected[i], result)
        }
    }
}