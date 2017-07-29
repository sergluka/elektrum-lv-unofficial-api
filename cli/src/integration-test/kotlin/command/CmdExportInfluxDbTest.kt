package command

import main
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
internal class CmdExportInfluxDbTest {

    @Test
    fun exportRecentDataPerHour() {
        main(arrayOf("influxdb", "--debug", "--period", "day", "--recent"))
    }

    @Test
    fun exportRecentDataPerDay() {
        main(arrayOf("influxdb", "--debug", "--period", "month", "--recent"))
    }

    @Test
    fun exportRecentDataPerMonth() {
        main(arrayOf("influxdb", "--debug", "--period", "year", "--recent"))
    }

    @Test
    fun exportDataPeriodPerHour() {
        main(arrayOf("influxdb", "--debug", "--period", "day", "2017-01-01", "2017-03-02"))
    }

    @Test
    fun exportDataPeriodPerDay() {
        main(arrayOf("influxdb", "--debug", "--period", "month", "2017-01", "2017-02"))
    }

    @Test
    fun exportDataPeriodPerMonth() {
        main(arrayOf("influxdb", "--debug", "--period", "year", "2016"))
    }
}
