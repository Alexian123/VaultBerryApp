package com.alexianhentiu.vaultberryapp.presentation.ui.common

import com.alexianhentiu.vaultberryapp.domain.model.CipherContext

/**
 * Represents a request from the ViewModel to the UI to display a biometric prompt.
 * Contains all necessary data for the prompt and for subsequent processing in the UI.
 */
sealed class BiometricPromptRequest {
    abstract val context: CipherContext

    data class Store(
        override val context: CipherContext
    ) : BiometricPromptRequest()

    data class Retrieve(
        override val context: CipherContext
    ) : BiometricPromptRequest()
}