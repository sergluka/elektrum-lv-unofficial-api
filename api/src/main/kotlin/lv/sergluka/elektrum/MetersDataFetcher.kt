package lv.sergluka.elektrum

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.UnexpectedPage
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.*
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoField

class MetersDataFetcher : AutoCloseable {
    private val log = LoggerFactory.getLogger(javaClass)

    private val webClient = WebClient(BrowserVersion.CHROME)

    private val URL_BASE = "https://mans.elektrum.lv/en/for-home"

    class AuthorizationError(message: String?) : Exception(message)
    class NoDataError(message: String?) : Exception(message)
    class ParsingError(message: String?, e: Throwable) : Exception(message, e)

    enum class Period(val value: String) {
        DAY("D"),
        MONTH("M"),
        YEAR("Y")
    }

    init {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").level = java.util.logging.Level.OFF
    }

    fun authorize(login: String, password: String) {

        val url = "$URL_BASE/authorization/"
        log.debug("Get page '$url'")

        val page = webClient.getPage<HtmlPage>(url)
        log.debug("Page has been opened")
        log.trace("Authorization page: ${page.asXml()}")

        val form = page.forms[0]!!
        val inputLogin: HtmlEmailInput = form.getInputByName("login")
        val inputPassword: HtmlPasswordInput = form.getInputByName("password")
        val buttons = form.getByXPath<HtmlButton>("//button[@id='login-submit']")
        if (buttons.size == 0) {
            throw Exception("Cannot find submit button")
        }
        val submitButton = buttons[0]

        inputLogin.valueAttribute = login
        inputPassword.valueAttribute = password

        log.debug("Clicking the button")
        val newPage: HtmlPage = submitButton.click()
        log.trace("Response page: ${newPage.asXml()}")

        newPage.getByXPath<HtmlSpan>("//span[@class='error']")?.let {
            if (!it.isEmpty()) {
                val errors = it.map { span -> span.firstChild.asText() }
                throw AuthorizationError("Authorization errors: $errors")
            }
        }
        log.debug("Authorization done")
    }

    fun fetchMonthly(from: Year): Map<LocalDate, Double?> {

        val jsonFormatter = DateTimeFormatterBuilder()
                .appendPattern("MM.yyyy.")
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter()

        return fetch(Period.YEAR, from.toString(), null, jsonFormatter)
    }

    fun fetchDaily(from: YearMonth): Map<LocalDate, Double?> {
        return fetch(Period.MONTH,
                from.format(DateTimeFormatter.ofPattern("yyyy-MM")), null,
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
        )
    }

    fun fetchHourly(from: LocalDate, to: LocalDate): Map<LocalDate, Double?> {
        val formatterUrl = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatterJson = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")
        return fetch(Period.DAY, from.format(formatterUrl), to.format(formatterUrl), formatterJson)
    }

    override fun close() {
        webClient.close()
    }

    private fun fetch(period: Period, from: String, to: String?, jsonFormatter: DateTimeFormatter)
            : Map<LocalDate, Double?> {

        val url = "$URL_BASE/consumption-report/smart-meters-data/consumption.json?" +
                "step=${period.value}&" +
                "fromDate=$from&" +
                "tillDate=$to&" +
                "useDateRange=${if (to != null) 1 else 0}"
        log.debug("MetersDataFetcher: $url")

        val page = webClient.getPage<UnexpectedPage>(url)
        log.debug("Response: ${page.webResponse.contentAsString}")

        return parse(page.webResponse.contentAsString, jsonFormatter)
    }

    private fun parse(content: String, formatter: DateTimeFormatter): Map<LocalDate, Double?> {

        val jsonObject = JsonParser().parse(content).asJsonObject
        val data = jsonObject.get("data")?.asJsonObject
        if (data == null) {
            val text = jsonObject.get("text")?.asString
            throw NoDataError(text ?: "No data")
        }

        val amounts = data.get("A+").asJsonArray
        val dates = jsonObject.get("texts").asJsonObject
                              .get("tooltips").asJsonObject
                              .get("A+").asJsonObject
                              .entrySet().first().value.asJsonArray
        val zipped = dates.zip(amounts)

        return zipped.map { joinDateAndAmount(it, formatter) }.toMap()
    }

    private fun joinDateAndAmount(pair: Pair<JsonElement, JsonElement>, formatter: DateTimeFormatter):
            Pair<LocalDate, Double?> {

        val date_str = pair.first.asString
        val amount: Double?
        try {
            val amountObj = pair.second.asJsonObject.entrySet().last().value
            amount = if (amountObj.isJsonNull) null else amountObj.asDouble
        } catch (e: Exception) {
            throw ParsingError("Fail to get amount object from: $pair", e)
        }

        val date: LocalDate
        try {
            date = LocalDate.parse(date_str, formatter)
        } catch (e: DateTimeParseException) {
            throw ParsingError("Fail to parse date '$date_str'", e)
        }

        return Pair(date, amount)
    }
}
