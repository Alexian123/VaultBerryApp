package com.alexianhentiu.vaultberryapp.presentation.utils.biometric

import com.alexianhentiu.vaultberryapp.domain.model.entity.EncryptedAuthCredentials
import javax.crypto.Cipher

/**
 * Represents a request from the ViewModel to the UI to display a biometric prompt.
 * Contains all necessary data for the prompt and for subsequent processing in the UI.
 */
sealed class BiometricPromptRequest {
    abstract val title: String
    abstract val subtitle: String
    abstract val cryptoObject: Cipher?

    data class Store(
        override val title: String,
        override val subtitle: String,
        override val cryptoObject: Cipher?
    ) : BiometricPromptRequest()

    data class Retrieve(
        override val title: String,
        override val subtitle: String,
        override val cryptoObject: Cipher?,
        val encryptedData: EncryptedAuthCredentials
    ) : BiometricPromptRequest()
}