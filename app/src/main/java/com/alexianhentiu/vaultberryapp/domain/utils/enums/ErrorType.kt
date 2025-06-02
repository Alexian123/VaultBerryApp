package com.alexianhentiu.vaultberryapp.domain.utils.enums

enum class ErrorType(
    val text: String
) {

    // API errors
    API("API error"),
    REQUIRES_2FA(""),
    SERVER_IDENTITY_VERIFICATION_FAILURE("Server identity verification failure"),

    // Use case errors
    KEY_DECRYPTION_FAILURE("Key decryption failure"),
    ENTRY_DECRYPTION_FAILURE("Entry decryption failure"),
    ENTRY_ENCRYPTION_FAILURE("Entry encryption failure"),
    TWO_FACTOR_SETUP_FAILURE("Two-factor setup failure"),
    KEY_GENERATION_FAILURE("Key generation failure"),
    PASSWORD_PAIR_GENERATION_FAILURE("Password pair generation failure"),
    MISSING_KEYCHAIN("Missing keychain"),
    COPY_TO_CLIPBOARD_FAILURE("Copy to clipboard failure"),
    PASSWORD_STRENGTH_EVALUATION_FAILURE("Password strength evaluation failure"),
    PASSWORD_GENERATION_FAILURE("Password generation failure"),
    LOAD_INPUT_VALIDATOR_FAILURE("Load input validator failure"),
    LOAD_SETTINGS_FAILURE("Load settings failure"),
    OBSERVE_SETTINGS_FAILURE("Observe settings failure"),
    SAVE_SETTINGS_FAILURE("Save settings failure"),

    // Other errors
    BIOMETRIC("Biometric error"),
    UNKNOWN("Unknown error")
}