import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth

import lv.sergluka.elektrum.MetersDataFetcher

/*
#. Command line, and input from file
#. Export to CSV
#. Export to InnoDB
 */

fun main(args : Array<String>) {

    val log = LoggerFactory.getLogger("main");

    MetersDataFetcher().use {
        it.authorize(login = "kudesnik@inbox.lv", password = "e{;3j8W...")

        val year = it.fetchMonthly(Year.of(2016))
        year.forEach{ log.info(">> $it") }

        val month = it.fetchDaily(YearMonth.of(2017, 2))
        month.forEach{ log.info(">> $it") }

        val days = it.fetchHourly(LocalDate.of(2017, 2, 1), LocalDate.of(2017, 2, 28))
        days.forEach{ log.info(">> $it") }
    }

}


