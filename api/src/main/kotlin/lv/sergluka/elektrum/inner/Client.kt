package lv.sergluka.elektrum.inner

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.Page
import com.gargoylesoftware.htmlunit.UnexpectedPage
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.*
import org.slf4j.LoggerFactory
import java.util.*

private const val URL_BASE: String = "https://mans.elektrum.lv/en/for-home"

class Client: AutoCloseable {

    companion object {
        private val log = LoggerFactory.getLogger(Client::class.java)
    }

    class AuthorizationError(message: String?) : Exception(message)
    class ContentError(message: String?) : Exception(message)

    internal enum class Period(val value: String) {
        DAY("D"),
        MONTH("M"),
        YEAR("Y")
    }

    private var authorized: Boolean = false
        get

    private val webClient: WebClient

    init {
        // This module spams too much
        java.util.logging.Logger.getLogger("com.gargoylesoftware").level = java.util.logging.Level.OFF

        val version  = BrowserVersion.CHROME
        version.systemTimezone = TimeZone.getTimeZone("UTC")

        webClient = WebClient(BrowserVersion.CHROME)
        webClient.getOptions().isCssEnabled = false;
        webClient.getOptions().isJavaScriptEnabled = true;
        webClient.getOptions().isThrowExceptionOnScriptError = false;
        webClient.getOptions().timeout = 10000;
    }

    override fun close() {
        webClient.close()
    }

    internal fun authorize(login: String, password: String) {

        authorized = false

        val url = "$URL_BASE/authorization/"
        log.debug("Getting page '$url'")

        val page = webClient.getPage<HtmlPage>(url)
        log.info("Page '$url' has been opened")
        log.trace("Authorization page: ${page.asXml()}")

        val form = page.forms[0]!!
        val inputLogin = form.getInputByName<HtmlEmailInput>("login")
        val inputPassword = form.getInputByName<HtmlPasswordInput>("password")
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

        checkForFailedAuthorization(url, newPage)

        log.info("Authorization is successful")
        authorized = true
    }

    internal fun fetch(period: Period, from: String, to: String?) : String {

        if (!authorized) {
            throw AuthorizationError("Not authorized")
        }

        val useRange = "useDateRange=${if (to != null) 1 else 0}"
        val tillDate = if (to != null) "tillDate=$to&" else ""
        val url = "$URL_BASE/consumption-report/smart-meters-data/consumption.json?" +
                  "step=${period.value}&" +
                  "fromDate=$from&" +
                  tillDate +
                  useRange
        log.debug("Get data from '$url'")

        val page = webClient.getPage<Page>(url)
        if (page !is UnexpectedPage) {
            if (page is HtmlPage) {
                throw ContentError("Got HTML instead of JSON from '$url': \n${page.asXml()}")
            }
            throw ContentError("Got unknown content instead of JSON from '$url'")
        }

        log.debug("Response: ${page.webResponse.contentAsString}")

        return page.webResponse.contentAsString
    }

    private fun checkForFailedAuthorization(url: String, page: HtmlPage) {

        fun lookForErrors(it: List<HtmlElement>) {
            if (it.isEmpty()) {
                return
            }
            val errors = it.map { element -> element.firstChild.asText() }
            throw AuthorizationError("Authorization errors: $errors. Captcha is possible. " +
                                                                       "Try to login via browser to reset it: $url")
        }

        lookForErrors(page.getByXPath<HtmlSpan>("//span[@class='error']"))
        lookForErrors(page.getByXPath<HtmlParagraph>("//div[@class='notification error']//p"))
    }

}
