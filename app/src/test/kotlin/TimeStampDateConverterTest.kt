import com.example.GymBuddy.data.localdatabase.DateTimestampCoverter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import org.junit.Test
import java.time.LocalDate

class TimeStampDateConverterTest {
    private val converter = DateTimestampCoverter()

    @Test
    fun dateToTimestamp_convertsCorrectly() {
        val localDate = LocalDate.of(2023, 8, 10)
        val timestamp = converter.dateToTimestamp(localDate)

        val expectedTimestamp = 1691625600000L
        assertEquals(expectedTimestamp, timestamp)
    }

    @Test
    fun dateToTimestamp_nullInput_returnsNull() {
        val timestamp = converter.dateToTimestamp(null)
        assertNull(timestamp)
    }

    @Test
    fun timeStampToDate_convertsCorrectly() {
        val timestamp = 1691625600000L
        val localDate = converter.timeStampToDate(timestamp)

        val expectedDate = LocalDate.of(2023, 8, 10)
        assertEquals(expectedDate, localDate)
    }

    @Test
    fun timeStampToDate_nullInput_returnsNull() {
        val localDate = converter.timeStampToDate(null)
        assertNull(localDate)
    }
}