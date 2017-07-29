package command

import main
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.io.File
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

const val FILENAME = "output.csv"

@RunWith(JUnitPlatform::class)
internal class CmdExportCsvTest {

    private val file = File(FILENAME)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    @BeforeEach
    fun cleanOutput() {
        if (file.exists()) {
            file.delete()
        }
    }

    @Test
    fun exportDataPeriodPerHour() {
        main(arrayOf("csv", "--output", FILENAME, "--debug", "--period", "day", "2017-01-01", "2017-03-02"))
        checkOutput(size = 1464, startTime = LocalDateTime.of(2017, Month.JANUARY, 1, 1, 0), unit = ChronoUnit.HOURS)
    }

    @Test
    fun exportDataPeriodPerDay() {
        main(arrayOf("csv", "--output", FILENAME, "--debug", "--period", "month", "2017-01", "2017-02"))
        checkOutput(size = 31, startTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0), unit = ChronoUnit.DAYS)
    }

    @Test
    fun exportDataPeriodPerMonth() {
        main(arrayOf("csv", "--output", FILENAME, "--debug", "--period", "year", "2016"))
        checkOutput(size = 12, startTime = LocalDateTime.of(2017, Month.JANUARY, 1, 0, 0), unit = ChronoUnit.MONTHS)
    }

    fun checkOutput(size: Int, startTime: LocalDateTime, unit: ChronoUnit) {
        val csv = getCsv()
        assertEquals(size, csv.size)

        var start = startTime
        csv.forEach { it ->
            assertEquals(2, it.size)

            val expected = start.format(formatter)
            assertEquals(expected, it[0])

            start = start.plus(1, unit)
        }
    }

    fun getCsv(): List<List<String>> {
        return file.readLines().map { it.split(';') }
    }
}
