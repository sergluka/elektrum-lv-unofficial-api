package lv.sergluka.elektrum.inner

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RunWith(JUnitPlatform::class)
internal class ParserTest {

    private val parser = Parser()
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")

    @Test
    fun parseNoData() {

        val json = """{
        |    "data": []
        |}""".trimMargin()

        assertTrue(parser.parse(json, formatter).isEmpty())
    }

    @Test
    fun parsePass() {

        val json = """{
        |    "data": {
        |        "A+": [
        |            {
        |                "date": "ignored",
        |                "ignored": "123.01"
        |            },
        |            {
        |                "date": "ignored",
        |                "ignored": "123.02"
        |            },
        |            {
        |                "date": "ignored",
        |                "ignored": "123.03"
        |            }
        |        ]
        |    },
        |    "texts": {
        |        "tooltips": {
        |            "A+": {
        |                "ignored": [
        |                    "01.05.2017. 01:00",
        |                    "01.05.2017. 02:00",
        |                    "01.05.2017. 03:00"
        |                ]
        |            }
        |        }
        |    }
        |}""".trimMargin()


        val res = parser.parse(json, formatter)
        assertEquals(3, res.size)
        assertEquals(123.01, res[LocalDateTime.of(2017, 5, 1, 1, 0)])
        assertEquals(123.02, res[LocalDateTime.of(2017, 5, 1, 2, 0)])
        assertEquals(123.03, res[LocalDateTime.of(2017, 5, 1, 3, 0)])
    }

    @Test
    fun parseWrongDate() {

        val json = """{
        |    "data": {
        |        "A+": [
        |            {
        |                "date": "ignored",
        |                "ignored": "123.01"
        |            }
        |        ]
        |    },
        |    "texts": {
        |        "tooltips": {
        |            "A+": {
        |                "ignored": [
        |                    "01.05.2017. 01:00:00",
        |                ]
        |            }
        |        }
        |    }
        |}""".trimMargin()


        assertThrows(Parser.ParsingError::class.java, { parser.parse(json, formatter) })
    }

    @Test
    fun parseWrongAmount() {

        val json = """{
        |    "data": {
        |        "A+": [
        |            {
        |                "date": "ignored",
        |                "ignored": "123,01"
        |            }
        |        ]
        |    },
        |    "texts": {
        |        "tooltips": {
        |            "A+": {
        |                "ignored": [
        |                    "01.05.2017. 01:00",
        |                ]
        |            }
        |        }
        |    }
        |}""".trimMargin()

        assertThrows(Parser.ParsingError::class.java, { parser.parse(json, formatter) })
    }

    @Test
    fun parseAmountAndDatesDifferentCounts() {

        val json = """{
        |    "data": {
        |        "A+": [
        |            {
        |                "date": "ignored",
        |                "ignored": "123.01"
        |            },
        |            {
        |                "date": "ignored",
        |                "ignored": "123.02"
        |            }
        |        ]
        |    },
        |    "texts": {
        |        "tooltips": {
        |            "A+": {
        |                "ignored": [
        |                    "01.05.2017. 01:00",
        |                    "01.05.2017. 02:00",
        |                    "01.05.2017. 03:00",
        |                ]
        |            }
        |        }
        |    }
        |}""".trimMargin()


        assertThrows(Parser.ParsingError::class.java, { parser.parse(json, formatter) })
    }
}
