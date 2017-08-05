package command

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import com.github.rvesse.airline.annotations.OptionType
import com.github.rvesse.airline.annotations.help.Examples
import com.github.rvesse.airline.annotations.restrictions.MutuallyExclusiveWith
import command.base.CmdCommonOptions
import command.base.CommandRunnable
import org.influxdb.InfluxDB
import org.influxdb.InfluxDB.ConsistencyLevel
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.BatchPoints
import org.influxdb.dto.Point
import org.influxdb.dto.Query
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import settings.Settings
import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

@Command(name = "influxdb", description = "Export into InfluxDB")
@Examples(examples = arrayOf("influxdb --period day --debug 2017-07-01 2017-08-01",
                             "influxdb --period day --recent"))
class CmdExportInfluxDb: CmdCommonOptions(), CommandRunnable {

    @Option(type = OptionType.COMMAND, name = arrayOf("-r", "--recent"), description = "Get recent data")
    @MutuallyExclusiveWith(tag = "dates")
    private val recent: Boolean = false

    private val log: Logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    lateinit private var db: InfluxDB
    lateinit private var dbName: String

    override fun run(settings: Settings.Data) {
        settings.influxdb.apply {
            db = InfluxDBFactory.connect(url, login, password.plain)
            dbName = database
        }
        db.createDatabase(dbName)
        db.setDatabase(dbName)

        if (recent) {
            val query = Query("SELECT LAST(value) FROM $periodStr", dbName)
            val result = db.query(query)
            val fromStr = result.results[0]?.series?.get(0)?.values?.get(0)?.get(0)?.toString()
            val from: LocalDateTime = if (fromStr != null) {
                LocalDateTime.parse(fromStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
            } else {
                log.warn("Incremental mode is on, but table '$periodStr' is empty. " +
                         "Since we cannot find the start point, importing all from 1 Jan 2016.")
                LocalDateTime.of(2016, Month.JANUARY, 1, 0, 0)
            }
            export(fetch(settings, from.toLocalDate()))
        } else {
            export(fetch(settings))
        }
    }

    private fun export(meters: Map<LocalDateTime, Double?>) {

        if (meters.isEmpty()) {
            log.info("No data to write")
            return
        }

        val batch = BatchPoints.database(dbName)
                                .consistency(ConsistencyLevel.ALL)
                                .build()

        meters.filter { it.value != null }
              .forEach { date, amount ->
            batch.point(
                    Point.measurement(periodStr)
                         .time(date.toEpochSecond(ZoneOffset.UTC), TimeUnit.SECONDS)
                         .addField("value", amount)
                         .build())
        }

        val count = batch.points.count()
        db.write(batch)

        log.info("$count points have been written")
    }
}
