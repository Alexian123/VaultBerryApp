package com.alexianhentiu.vaultberryapp.domain.utils.security

import com.alexianhentiu.vaultberryapp.domain.model.entity.KeyChain
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.CryptographyHandler
import java.security.SecureRandom
import java.util.Base64

class VaultGuardian(
    private val cryptoHandler: CryptographyHandler
) {
    private val ivLength = 12
    private val saltLength = 16
    private val secureRandom = SecureRandom()

    /**
     * Generates a keychain with new or existing vault key
     * @param password The password used to encrypt the vault key
     * @param recoveryPassword The password used to encrypt the recovery key
     * @param decryptedKey The existing vault key, null if a new key should be generated
     * @return The generated KeyChain consisting of the salt, the vault key and the recovery key
     */
    fun generateKeyChain(
        password: String,
        recoveryPassword: String,
        decryptedKey: ByteArray? = null
    ): KeyChain {
        val vaultKey =
            decryptedKey ?: ByteArray(32).apply { secureRandom.nextBytes(this) }
        val salt = secureRandom.generateSeed(saltLength)

        // encrypt vault key
        val keyIV = ByteArray(ivLength).apply { secureRandom.nextBytes(this) }
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        val encryptedVaultKey = cryptoHandler.encrypt(vaultKey, derivedKey.encoded, keyIV)

        // encrypt recovery key
        val recoveryIV = ByteArray(ivLength).apply { secureRandom.nextBytes(this) }
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
     * @param password The password used to encrypt the vault key
     * @param encodedSalt The Base64 encoded salt
     * @param encodedKey The Base64 encoded encrypted vault key
     * @return The decrypted vault key as a ByteArray
     */
    fun decryptKey(password: String, encodedSalt: String, encodedKey: String): ByteArray {
        val encryptedBytes = encodedKey.fromBase64()
        val iv = encryptedBytes.sliceArray(0 until ivLength)
        val encryptedVaultKey = encryptedBytes.sliceArray(ivLength until encryptedBytes.size)
        val salt = encodedSalt.fromBase64()
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        return cryptoHandler.decrypt(encryptedVaultKey, derivedKey.encoded, iv)
    }

    /**
     * Encrypts the text field with the vault key
     * @param plainText The text to be encrypted
     * @param decryptedKey The decrypted vault key
     * @return The encrypted field concatenated to the IV as a Base64 string
     */
    fun encryptField(plainText: String, decryptedKey: ByteArray): String {
        val iv = ByteArray(ivLength).apply { SecureRandom().nextBytes(this) }
        val encryptedFieldData =
            cryptoHandler.encrypt(plainText.toByteArray(), decryptedKey, iv)
        return (iv + encryptedFieldData).toBase64()
    }

    /**
     * Retrieves the IV and decrypts the field.
     * @param encryptedField The encrypted field
     * @param decryptedKey The decrypted vault key
     * @return The decrypted field as a String
     */
    fun decryptField(encryptedField: String, decryptedKey: ByteArray): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedField)
        val iv = encryptedBytes.sliceArray(0 until ivLength)
        val encryptedFieldData = encryptedBytes.sliceArray(ivLength until encryptedBytes.size)
        val decryptedFieldData = cryptoHandler.decrypt(encryptedFieldData, decryptedKey, iv)
        return String(decryptedFieldData)
    }

    private fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

    private fun String.fromBase64(): ByteArray = Base64.getDecoder().decode(this)
}