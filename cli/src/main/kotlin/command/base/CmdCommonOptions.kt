package command.base

import ch.qos.logback.classic.Level
import com.github.rvesse.airline.annotations.Arguments
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.OptionType
import com.github.rvesse.airline.annotations.restrictions.AllowedRawValues
import com.github.rvesse.airline.annotations.restrictions.MaxOccurrences
import com.github.rvesse.airline.annotations.restrictions.MutuallyExclusiveWith
import com.github.rvesse.airline.annotations.restrictions.Required
import lv.sergluka.elektrum.ElektrumFetcher
import org.slf4j.LoggerFactory
import settings.Settings
import java.time.LocalDate
import java.time.LocalDateTime

abstract class CmdCommonOptions {

    protected enum class Period(val value: String) {
        DAY("day"),
        MONTH("month"),
        YEAR("year");

        companion object {
            private val map = Period.values().associateBy(Period::value)
            fun fromString(type: String) = map[type] ?:
                    throw IllegalArgumentException("Enum '$type' doesn't exists")
        }
    }

    @Option(type = OptionType.COMMAND, name = arrayOf("--period"), description = "Date period")
    @AllowedRawValues(allowedValues = arrayOf("day", "month", "year"))
    @Required
    protected val periodStr: String = ""

    @Option(type = OptionType.COMMAND, name = arrayOf("--info"), description = "Switch log level to INFO")
    @MutuallyExclusiveWith(tag = "logging")
    private val info = false

    @Option(type = OptionType.COMMAND, name = arrayOf("--debug"), description = "Switch log level to DEBUG")
    @MutuallyExclusiveWith(tag = "logging")
    private val debug = false

    @Option(type = OptionType.COMMAND, name = arrayOf("--trace"), description = "Switch log level to TRACE")
    @MutuallyExclusiveWith(tag = "logging")
    private val trace = false

    @Arguments(title = arrayOf("dates"), description = "Dates")
    @MutuallyExclusiveWith(tag = "dates")
    @MaxOccurrences(occurrences = 2)
    protected var dates: List<LocalDate> = mutableListOf()

    protected val period
        get() = Period.fromString(periodStr)

    fun fetch(settings: Settings.Data, from: LocalDate? = dates.getOrNull(0), to: LocalDate? = dates.getOrNull(1)):
            Map<LocalDateTime, Double?> {

        setLogLevel()
        return fetchData(settings.elektrum, period, from!!, to)
    }

    private fun fetchData(credential: Settings.Credentials,
                          period: Period,
                          from: LocalDate,
                          to: LocalDate?): Map<LocalDateTime, Double?> {
        ElektrumFetcher().use {
            it.authorize(credential.login, credential.password.plain)
            val meters = when (period) {
                Period.YEAR -> it.fetchMonthly(from, to)
                Period.MONTH -> it.fetchDaily(from, to)
                Period.DAY -> it.fetchHourly(from, to)
            }

            return meters
        }
    }

    private fun setLogLevel() {
        val level = when {
            info -> Level.INFO
            debug -> Level.DEBUG
            trace -> Level.TRACE
            else -> Level.WARN
        }

        val root = LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME)
                as ch.qos.logback.classic.Logger
        root.level = level
    }
}
