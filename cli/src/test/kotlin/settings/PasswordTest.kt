package settings

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
internal class PasswordTest {

    lateinit var password: Password

    @BeforeEach
    fun setup() {
        password = Password()
    }

    @Test
    fun encryptionAndDecriptionMatches() {
        password.plain = "some string"

        assertTrue(password.encrypted.isEmpty())
        password.process()
        password.plain = ""
        assertFalse(password.encrypted.isEmpty())

        password.process()
        assertEquals("some string", password.plain)
    }
}
