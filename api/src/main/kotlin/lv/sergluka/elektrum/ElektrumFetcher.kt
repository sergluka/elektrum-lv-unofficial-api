package lv.sergluka.elektrum

import lv.sergluka.elektrum.inner.Client
import lv.sergluka.elektrum.inner.Parser
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

class ElektrumFetcher: AutoCloseable {

    companion object {
        private val log = LoggerFactory.getLogger(Client::class.java)
    }

    private val client = Client()
    private val parser = Parser()

    fun authorize(login: String, password: String) = client.authorize(login, password)

    fun fetchMonthly(from: LocalDate, to: LocalDate?) : Map<LocalDateTime, Double?> {
        val formatterUrl = DateTimeFormatter.ofPattern("yyyy")
        val formatterJson = DateTimeFormatterBuilder()
                .appendPattern("MM.yyyy.")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter()
        return fetchContinuous(Client.Period.YEAR, from, to, formatterUrl, formatterJson,
                               useRange = false, increment = ChronoUnit.YEARS)
    }

    fun fetchDaily(from: LocalDate, to: LocalDate?) : Map<LocalDateTime, Double?> {
        val formatterUrl = DateTimeFormatter.ofPattern("yyyy-MM")
        val formatterJson = DateTimeFormatterBuilder()
                .appendPattern("dd.MM.yyyy")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .toFormatter()
        return fetchContinuous(Client.Period.MONTH, from, to, formatterUrl, formatterJson,
                               useRange = false, increment = ChronoUnit.MONTHS)
    }

    fun fetchHourly(from: LocalDate, to: LocalDate?) : Map<LocalDateTime, Double?> {
        val formatterUrl = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterJson = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")
        return fetchContinuous(Client.Period.DAY, from, to, formatterUrl, formatterJson,
                               useRange = true, increment = ChronoUnit.DAYS)
    }

    private fun fetchContinuous(period: Client.Period, from: LocalDate, to: LocalDate?,
                                formatterUrl: DateTimeFormatter, formatterJson: DateTimeFormatter,
                                useRange: Boolean, increment: ChronoUnit)
            : Map<LocalDateTime, Double?> {

        val ret: MutableMap<LocalDateTime, Double?> = mutableMapOf()
        var reqStart = from
        val reqEnd = to ?: LocalDate.now().plusDays(1)
        while (reqStart < reqEnd) {

            val json = client.fetch(period, reqStart.format(formatterUrl),
                                    if (useRange) reqEnd!!.format(formatterUrl) else null)
            val data = parser.parse(json, formatterJson)

            val respEnd = if (!data.isEmpty() && useRange) {
                data.keys.last().toLocalDate()
            } else {
                reqStart.plus(1, increment)
            }

            log.info("From $reqStart to $respEnd: ${data.size} points")
            ret.putAll(data)

            reqStart = respEnd
        }

        return ret
    }

    override fun close() {
        client.close()
    }
}
