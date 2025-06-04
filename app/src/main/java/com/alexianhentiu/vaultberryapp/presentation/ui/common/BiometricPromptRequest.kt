package com.alexianhentiu.vaultberryapp.presentation.ui.common

import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import javax.crypto.Cipher

/**
 * Represents a request from the ViewModel to the UI to display a biometric prompt.
 * Contains all necessary data for the prompt and for subsequent processing in the UI.
 */
sealed class BiometricPromptRequest {
    abstract val cryptoObject: Cipher?

    data class Store(
        override val cryptoObject: Cipher?
    ) : BiometricPromptRequest()

    data class Retrieve(
        override val cryptoObject: Cipher?,
        val encryptedData: EncryptedAuthCredentials
    ) : BiometricPromptRequest()
}