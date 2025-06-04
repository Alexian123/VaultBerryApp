package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import java.security.SecureRandom
import java.util.Base64

class VaultGuardian(
    private val cryptoHandler: GeneralCryptoHandler
) : VaultSecurityHandler {
    private val ivLength = 12
    private val saltLength = 16
    private val secureRandom = SecureRandom()

    override fun generateKeyChain(
        password: String,
        recoveryPassword: String,
        decryptedKey: ByteArray?
    ): KeyChain {
        val vaultKey =
            decryptedKey ?: ByteArray(32).apply { secureRandom.nextBytes(this) }
        val salt = secureRandom.generateSeed(saltLength)

        // encrypt vault key
        val keyIV = ByteArray(ivLength).apply { secureRandom.nextBytes(this) }
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        val encryptedVaultKey = cryptoHandler.encrypt(vaultKey, derivedKey, keyIV)

        // encrypt recovery key
        val recoveryIV = ByteArray(ivLength).apply { secureRandom.nextBytes(this) }
        val derivedRecoveryKey = cryptoHandler.deriveKeyFromPassword(recoveryPassword, salt)
        val encryptedRecoveryKey = cryptoHandler.encrypt(vaultKey, derivedRecoveryKey,
            recoveryIV)

        return KeyChain(
            salt = salt.toBase64(),
            vaultKey = (keyIV + encryptedVaultKey).toBase64(),
            recoveryKey = (recoveryIV + encryptedRecoveryKey).toBase64()
        )
    }

    override fun decryptKey(password: String, encodedSalt: String, encodedKey: String): ByteArray {
        val encryptedBytes = encodedKey.fromBase64()
        val iv = encryptedBytes.sliceArray(0 until ivLength)
        val encryptedVaultKey = encryptedBytes.sliceArray(ivLength until encryptedBytes.size)
        val salt = encodedSalt.fromBase64()
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        return cryptoHandler.decrypt(encryptedVaultKey, derivedKey, iv)
    }

    override fun encryptField(plainText: String, decryptedKey: ByteArray): String {
        val iv = ByteArray(ivLength).apply { SecureRandom().nextBytes(this) }
        val encryptedFieldData =
            cryptoHandler.encrypt(plainText.toByteArray(), decryptedKey, iv)
        return (iv + encryptedFieldData).toBase64()
    }

    override fun decryptField(encryptedField: String, decryptedKey: ByteArray): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedField)
        val iv = encryptedBytes.sliceArray(0 until ivLength)
        val encryptedFieldData = encryptedBytes.sliceArray(ivLength until encryptedBytes.size)
        val decryptedFieldData = cryptoHandler.decrypt(encryptedFieldData, decryptedKey, iv)
        return String(decryptedFieldData)
    }

    private fun ByteArray.toBase64(): String = Base64.getEncoder().encodeToString(this)

    private fun String.fromBase64(): ByteArray = Base64.getDecoder().decode(this)
}