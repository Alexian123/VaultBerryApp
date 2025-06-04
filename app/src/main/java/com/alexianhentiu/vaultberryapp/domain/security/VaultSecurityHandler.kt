package com.alexianhentiu.vaultberryapp.domain.security

import com.alexianhentiu.vaultberryapp.domain.model.KeyChain

interface VaultSecurityHandler {

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
    ): KeyChain

    /**
     * Retrieves the IV and decrypts the vault key.
     * @param password The password used to encrypt the vault key
     * @param encodedSalt The Base64 encoded salt
     * @param encodedKey The Base64 encoded encrypted vault key
     * @return The decrypted vault key as a ByteArray
     */
    fun decryptKey(password: String, encodedSalt: String, encodedKey: String): ByteArray

    /**
     * Encrypts the text field with the vault key
     * @param plainText The text to be encrypted
     * @param decryptedKey The decrypted vault key
     * @return The encrypted field concatenated to the IV as a Base64 string
     */
    fun encryptField(plainText: String, decryptedKey: ByteArray): String

    /**
     * Retrieves the IV and decrypts the field.
     * @param encryptedField The encrypted field
     * @param decryptedKey The decrypted vault key
     * @return The decrypted field as a String
     */
    fun decryptField(encryptedField: String, decryptedKey: ByteArray): String
}