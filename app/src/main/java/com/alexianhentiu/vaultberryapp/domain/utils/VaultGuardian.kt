package com.alexianhentiu.vaultberryapp.domain.utils

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.CryptographyHandler
import java.security.SecureRandom
import java.util.Base64

class VaultGuardian(
    private val cryptoHandler: CryptographyHandler
) {

    private val secureRandom = SecureRandom()

    /**
     * Generates and encrypts a vault key,
     * once with a key generated from the password
     * and once with a key generated from the recovery password
     * and appends it to the IV.
     * @return The generated KeyChain consisting of the salt, the vault key and the recovery key
     */
    fun generateKeyChain(password: String, recoveryPassword: String): KeyChain {
        val vaultKey = cryptoHandler.generateKey().encoded
        val salt = ByteArray(16).apply { secureRandom.nextBytes(this) }

        // encrypt vault key
        val keyIV = ByteArray(16).apply { secureRandom.nextBytes(this) }
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        val encryptedVaultKey = cryptoHandler.encrypt(vaultKey, derivedKey.encoded, keyIV)

        // encrypt recovery key
        val recoveryIV = ByteArray(16).apply { secureRandom.nextBytes(this) }
        val derivedRecoveryKey = cryptoHandler.deriveKeyFromPassword(recoveryPassword, salt)
        val encryptedRecoveryKey = cryptoHandler.encrypt(vaultKey, derivedRecoveryKey.encoded,
            recoveryIV)

        return KeyChain(
            salt = salt.toBase64(),
            vaultKey = (keyIV + encryptedVaultKey).toBase64(),
            recoveryKey = (recoveryIV + encryptedRecoveryKey).toBase64()
        )
    }

    /**
     * Retrieves the IV and decrypts the vault key.
     * @return The decrypted vault key as a ByteArray
     */
    fun decryptKey(password: String, encodedSalt: String, encodedKey: String): ByteArray {
        val encryptedBytes = encodedKey.fromBase64()
        val iv = encryptedBytes.sliceArray(0 until 16)
        val encryptedVaultKey = encryptedBytes.sliceArray(16 until encryptedBytes.size)
        val salt = encodedSalt.fromBase64()
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        return cryptoHandler.decrypt(encryptedVaultKey, derivedKey.encoded, iv)
    }

    /**
     * Encrypts the text field with the vault key and appends it to the IV.
     * @return The encrypted field concatenated to the IV as a Base64 string
     */
    fun encryptField(plainText: String, key: ByteArray): String {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val encryptedFieldData =
            cryptoHandler.encrypt(plainText.toByteArray(), key, iv)
        return Base64.getEncoder().encodeToString(iv + encryptedFieldData)
    }

    /**
     * Retrieves the IV and decrypts the field.
     * @return The decrypted field as a String
     */
    fun decryptField(encryptedField: String, key: ByteArray): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedField)
        val iv = encryptedBytes.sliceArray(0 until 16)
        val encryptedFieldData = encryptedBytes.sliceArray(16 until encryptedBytes.size)
        val decryptedFieldData = cryptoHandler.decrypt(encryptedFieldData, key, iv)
        return String(decryptedFieldData)
    }

    private fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

    private fun String.fromBase64(): ByteArray = Base64.getDecoder().decode(this)
}