package settings

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import oshi.SystemInfo
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

const val encryptionPass = "Y2lwaGVyLmluaXQoQ2lwaGVyLkVOQ1JZUFRfTU9ERSwga2V5KQ"

data class Password(var plain: String = "", var encrypted: String = "") {

    private val log: Logger = LoggerFactory.getLogger(this::class.java.canonicalName)

    fun process(): Boolean {
        var changed = false
        val key = createSecretKey(encryptionPass.toCharArray(), calcSalt().toByteArray(),
                                  iterationCount = 40000, keyLength = 128)

        if (plain.isEmpty() && !encrypted.isEmpty()) {
            plain = decrypt(encrypted, key)
        } else if (!plain.isEmpty()) {
            encrypted = encrypt(plain, key)
            changed = true
        } else {
            throw IllegalArgumentException("Password is empty")
        }

        return changed
    }

    private fun calcSalt(): String {
        try {
            return SystemInfo().hardware.processor.processorID!!
        } catch (e: Exception) {
            log.warn("Fail to calculate salt for passwords encryption", e)
            return "85f74d7b93cb8cbd74ac5a7f53a00b80"
        }
    }

    @Throws(GeneralSecurityException::class, UnsupportedEncodingException::class)
    private fun encrypt(password: String, key: SecretKeySpec): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val parameters = cipher.parameters
        val ivParameterSpec = parameters.getParameterSpec(IvParameterSpec::class.java)
        val cryptoText = cipher.doFinal(password.toByteArray(charset("UTF-8")))
        val iv = ivParameterSpec.iv
        return base64Encode(iv) + ":" + base64Encode(cryptoText)
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    private fun decrypt(string: String, key: SecretKeySpec): String {
        val iv = string.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        val property = string.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(base64Decode(iv)))
        val array: ByteArray = cipher.doFinal(base64Decode(property))
        return array.toString(Charset.forName("UTF-8"))
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun createSecretKey(password: CharArray, salt: ByteArray, iterationCount: Int, keyLength: Int)
            : SecretKeySpec {

        val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        val keySpec = PBEKeySpec(password, salt, iterationCount, keyLength)
        val keyTmp = keyFactory.generateSecret(keySpec)
        return SecretKeySpec(keyTmp.encoded, "AES")
    }

    private fun base64Encode(bytes: ByteArray): String {
        return Base64.getEncoder().encodeToString(bytes)
    }

    @Throws(IOException::class)
    private fun base64Decode(property: String): ByteArray {
        return Base64.getDecoder().decode(property)
    }
}
