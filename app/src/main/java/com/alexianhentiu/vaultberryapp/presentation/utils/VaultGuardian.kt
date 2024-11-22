package com.alexianhentiu.vaultberryapp.presentation.utils

import java.security.SecureRandom
import java.util.Base64

class VaultGuardian {

    companion object {

        /**
         * Generates and encrypts a vault key with a key generated from the password
         * and appends it to the IV.
         * @return The encrypted vault key concatenated to the IV as a Base64 string
         * and the salt as a Base64 string
         */
        fun exportVaultKey(password: String): Pair<String, String> {
            val vaultKey = Cryptography.generateKey().encoded
            val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
            val salt = Cryptography.generateSaltFromTimestamp()
            val derivedKey = Cryptography.deriveKeyFromPassword(password, salt)
            val encryptedVaultKey = Cryptography.encrypt(vaultKey, derivedKey.encoded, iv)
            return Pair(Base64.getEncoder().encodeToString(iv + encryptedVaultKey),
                Base64.getEncoder().encodeToString(salt))
        }

        /**
         * Retrieves the IV and decrypts the vault key.
         * @return The decrypted vault key as a ByteArray
         */
        fun importVaultKey(vaultKey: String, password: String, salt: String): ByteArray {
            val encryptedBytes = Base64.getDecoder().decode(vaultKey)
            val iv = encryptedBytes.sliceArray(0 until 16)
            val encryptedVaultKey = encryptedBytes.sliceArray(16 until encryptedBytes.size)
            val derivedKey =
                Cryptography.deriveKeyFromPassword(password, Base64.getDecoder().decode(salt))
            return Cryptography.decrypt(encryptedVaultKey, derivedKey.encoded, iv)
        }

        /**
         * Encrypts the text field with the vault key and appends it to the IV.
         * @return The encrypted field concatenated to the IV as a Base64 string
         */
        fun exportField(plainText: String, vaultKey: ByteArray): String {
            val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
            val encryptedField = Cryptography.encrypt(plainText.toByteArray(), vaultKey, iv)
            return Base64.getEncoder().encodeToString(iv + encryptedField)
        }

        /**
         * Retrieves the IV and decrypts the field.
         * @return The decrypted field as a String
         */
        fun importField(encryptedField: String, vaultKey: ByteArray): String {
            val encryptedBytes = Base64.getDecoder().decode(encryptedField)
            val iv = encryptedBytes.sliceArray(0 until 16)
            val encryptedVaultKey = encryptedBytes.sliceArray(16 until encryptedBytes.size)
            val decryptedField = Cryptography.decrypt(encryptedVaultKey, vaultKey, iv)
            return String(decryptedField)
        }
    }
}