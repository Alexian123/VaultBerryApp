package com.alexianhentiu.vaultberryapp.domain.utils

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedVaultKey
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.CryptographyHandler
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Base64

class VaultGuardian(
    private val cryptoHandler: CryptographyHandler
) {

    /**
     * Generates and encrypts a vault key with a key generated from the password
     * and appends it to the IV.
     * @return The encrypted vault key concatenated to the IV as a Base64 string
     * and the salt as a Base64 string
     */
    fun exportNewVaultKey(password: String): EncryptedVaultKey {
        val vaultKey = cryptoHandler.generateKey().encoded
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val salt = generateSaltFromTimestamp()
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password, salt)
        val encryptedVaultKey = cryptoHandler.encrypt(vaultKey, derivedKey.encoded, iv)
        return EncryptedVaultKey(Base64.getEncoder().encodeToString(iv + encryptedVaultKey),
            Base64.getEncoder().encodeToString(salt))
    }

    /**
     * Retrieves the IV and decrypts the vault key.
     * @return The decrypted vault key as a ByteArray
     */
    fun importExistingVaultKey(
        password: String,
        encryptedVaultKeyData: EncryptedVaultKey
    ): DecryptedVaultKey {
        val encryptedBytes = Base64.getDecoder().decode(encryptedVaultKeyData.ivAndKey)
        val iv = encryptedBytes.sliceArray(0 until 16)
        val encryptedVaultKey = encryptedBytes.sliceArray(16 until encryptedBytes.size)
        val derivedKey = cryptoHandler.deriveKeyFromPassword(password,
            Base64.getDecoder().decode(encryptedVaultKeyData.salt))
        return DecryptedVaultKey(cryptoHandler.decrypt(encryptedVaultKey, derivedKey.encoded, iv))
    }

    /**
     * Encrypts the text field with the vault key and appends it to the IV.
     * @return The encrypted field data concatenated to the IV as a Base64 string
     */
    fun exportField(plainText: String, decryptedVaultKey: DecryptedVaultKey): String {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val encryptedFieldData =
            cryptoHandler.encrypt(plainText.toByteArray(), decryptedVaultKey.key, iv)
        return Base64.getEncoder().encodeToString(iv + encryptedFieldData)
    }

    /**
     * Retrieves the IV and decrypts the field.
     * @return The decrypted field data as a String
     */
    fun importField(encryptedField: String, decryptedVaultKey: DecryptedVaultKey): String {
        val encryptedBytes = Base64.getDecoder().decode(encryptedField)
        val iv = encryptedBytes.sliceArray(0 until 16)
        val encryptedFieldData = encryptedBytes.sliceArray(16 until encryptedBytes.size)
        val decryptedFieldData = cryptoHandler.decrypt(encryptedFieldData, decryptedVaultKey.key, iv)
        return String(decryptedFieldData)
    }

    /**
     * Generates a salt from the current timestamp and a random set of bytes.
     * @return The salt as a ByteArray
     */
    private fun generateSaltFromTimestamp(): ByteArray {
        val timestamp = System.currentTimeMillis()
        val randomBytes = ByteArray(8)
        java.util.Random().nextBytes(randomBytes)
        val byteBuffer = ByteBuffer.allocate(16)
        byteBuffer.putLong(timestamp)
        byteBuffer.put(randomBytes)
        return byteBuffer.array()
    }
}