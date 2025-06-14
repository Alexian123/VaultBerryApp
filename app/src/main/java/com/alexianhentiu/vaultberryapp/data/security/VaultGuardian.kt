package com.alexianhentiu.vaultberryapp.data.security

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import com.alexianhentiu.vaultberryapp.domain.security.KeyDerivationHandler
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import java.security.SecureRandom
import java.util.Base64

class VaultGuardian(
    private val base64Handler: Base64Handler,
    private val cryptoHandler: GeneralCryptoHandler,
    private val keyDerivationHandler: KeyDerivationHandler
) : VaultSecurityHandler {

    private val saltLength = 16

    override fun generateKeyChain(
        password: String,
        recoveryPassword: String,
        decryptedKey: ByteArray?
    ): KeyChain {
        val vaultKey = decryptedKey ?: cryptoHandler.generateKey()
        val salt = cryptoHandler.generateSalt(saltLength)

        // encrypt vault key
        val keyIV = cryptoHandler.generateIV()
        val derivedKey = keyDerivationHandler.deriveKeyFromPassword(password, salt)
        val encryptedVaultKey = cryptoHandler.encrypt(vaultKey, derivedKey, keyIV)

        // encrypt recovery key
        val recoveryIV = cryptoHandler.generateIV()
        val derivedRecoveryKey = keyDerivationHandler.deriveKeyFromPassword(recoveryPassword, salt)
        val encryptedRecoveryKey = cryptoHandler.encrypt(vaultKey, derivedRecoveryKey,
            recoveryIV)

        return KeyChain(
            salt = base64Handler.encode(salt),
            vaultKey = base64Handler.encode(keyIV + encryptedVaultKey),
            recoveryKey = base64Handler.encode(recoveryIV + encryptedRecoveryKey)
        )
    }

    override fun decryptKey(password: String, encodedSalt: String, encodedKey: String): ByteArray {
        val encryptedBytes = base64Handler.decode(encodedKey)
        val iv = encryptedBytes.sliceArray(0 until cryptoHandler.ivLengthBytes)
        val encryptedVaultKey = encryptedBytes.sliceArray(
            cryptoHandler.ivLengthBytes until encryptedBytes.size
        )
        val salt = base64Handler.decode(encodedSalt)
        val derivedKey = keyDerivationHandler.deriveKeyFromPassword(password, salt)
        return cryptoHandler.decrypt(encryptedVaultKey, derivedKey, iv)
    }

    override fun encryptField(plainText: String, decryptedKey: ByteArray): String {
        val iv = ByteArray(cryptoHandler.ivLengthBytes).apply { SecureRandom().nextBytes(this) }
        val encryptedFieldData =
            cryptoHandler.encrypt(plainText.toByteArray(), decryptedKey, iv)
        return base64Handler.encode(iv + encryptedFieldData)
    }

    override fun decryptField(encryptedField: String, decryptedKey: ByteArray): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedField)
        val iv = encryptedBytes.sliceArray(0 until cryptoHandler.ivLengthBytes)
        val encryptedFieldData = encryptedBytes.sliceArray(
            cryptoHandler.ivLengthBytes until encryptedBytes.size
        )
        val decryptedFieldData = cryptoHandler.decrypt(encryptedFieldData, decryptedKey, iv)
        return String(decryptedFieldData)
    }
}