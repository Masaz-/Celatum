package tf.masaz.celatum.secret

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

const val IV_LENGTH = 128

class Secret {
    private var key: SecretKey? = null

    init {
        if (!isKeyPresent()) {
            generateKey()
        }

        key = getKey()
    }

    fun encryptText(textToEncrypt: String): Pair<String, ByteArray> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        // Encrypt the text
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(textToEncrypt.toByteArray())

        // Encode the encrypted bytes as Base64 string
        val encryptedText = Base64.getEncoder().encodeToString(encryptedBytes)
        return Pair(encryptedText, iv) // Return both encrypted text and IV
    }

    fun decryptText(encryptedText: String, iv: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        // Use GCMParameterSpec to provide the IV
        val gcmParameterSpec = GCMParameterSpec(IV_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)

        // Decode the Base64 encoded string and decrypt
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val decryptedBytes = cipher.doFinal(decodedBytes)

        return String(decryptedBytes)
    }

    private fun isKeyPresent(): Boolean {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return keyStore.containsAlias(KEY_NAME)
    }

    private fun getKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_NAME,KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()
        keyGenerator.init(keyGenParameterSpec)

        return keyGenerator.generateKey()
    }

    companion object {
        var KEY_NAME: String = "ba7b7d4c1687e6d03179867da1324a33"
    }
}