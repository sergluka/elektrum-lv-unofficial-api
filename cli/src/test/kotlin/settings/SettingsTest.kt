package settings

import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
internal class SettingsTest {

    private lateinit var settings: Settings

    @BeforeEach
    fun setUp() {
        settings = spy<Settings>()
    }

    @AfterEach
    fun tearDown() {
        reset(settings)
    }

    @Test
    fun plainPasswordsAreUncryptedAndRemovedBeforeWrite() {

        val yaml = """
               |elektrum:
               |  login: login
               |  password:
               |    plain: pass
               |    encrypted: none
               |influxdb:
               |  url: http://192.168.99.100:8086
               |  database: elektrum
               |  login: root
               |  password:
               |    plain: root
               |    encrypted: none
               """.trimMargin()

        doReturn(yaml).`when`(settings).read("test.yml")

        settings.get("test.yml") {
            assertEquals("login", it.elektrum.login)
            assertEquals("pass", it.elektrum.password.plain)
            assertFalse(it.elektrum.password.encrypted.isEmpty())
        }

        val captor = argumentCaptor<Settings.Data>()
        verify(settings).write(eq("test.yml"), captor.capture())

        assertTrue(captor.firstValue.elektrum.password.plain.isEmpty())
        assertFalse(captor.firstValue.elektrum.password.encrypted.isEmpty())
        assertTrue(captor.firstValue.influxdb.password.plain.isEmpty())
        assertFalse(captor.firstValue.influxdb.password.encrypted.isEmpty())
        assertEquals("http://192.168.99.100:8086", captor.firstValue.influxdb.url)
        assertEquals("elektrum", captor.firstValue.influxdb.database)
    }
}
