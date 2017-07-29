package lv.sergluka.elektrum.inner

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Parser {

    class ParsingError(message: String?, e: Throwable? = null) : Exception(message, e)

    companion object {
        private val log = LoggerFactory.getLogger(Parser::class.java)
    }

    internal fun parse(content: String, formatter: DateTimeFormatter): Map<LocalDateTime, Double?> {
        try {
            val jsonObject = JsonParser().parse(content).asJsonObject
            val dataRef = jsonObject.get("data")

            if (dataRef.isJsonArray && dataRef.asJsonArray.size() == 0) {
                log.debug("No data")
                return mapOf()
            }

            val data = jsonObject.get("data").asJsonObject
            val amounts = data.get("A+").asJsonArray
            val dates = jsonObject.get("texts").asJsonObject
                    .get("tooltips").asJsonObject
                    .get("A+").asJsonObject
                    .entrySet().first().value.asJsonArray
            val zipped = dates.zip(amounts)

            if (zipped.count() != dates.count() || zipped.count() != amounts.count()) {
                throw Exception("Amounts and dates counts are different")
            }

            return zipped.map { joinDateAndAmount(it, formatter) }.toMap()
        } catch (e: Exception) {
            throw ParsingError("Content parsing error", e)
        }
    }

    private fun joinDateAndAmount(pair: Pair<JsonElement, JsonElement>, formatter: DateTimeFormatter):
            Pair<LocalDateTime, Double?> {

        val date_str = pair.first.asString
        val amount: Double?
        try {
            val amountObj = pair.second.asJsonObject.entrySet().last().value
            val amountStr = if (!amountObj.isJsonNull) amountObj.asString else null
            amount = if (!amountStr.isNullOrEmpty()) amountStr!!.toDouble() else null
        } catch (e: Exception) {
            throw ParsingError("Fail to get amount object from: $pair", e)
        }

        val date: LocalDateTime
        try {
            date = LocalDateTime.parse(date_str, formatter)
        } catch (e: DateTimeParseException) {
            throw ParsingError("Fail to parse date '$date_str'", e)
        }

        return Pair(date, amount)
    }
}
