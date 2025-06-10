package com.alexianhentiu.vaultberryapp.domain.common.enums

enum class ErrorType(
    val text: String
) {

    // API errors
    API("API error"),
    SERVER_IDENTITY_VERIFICATION_FAILURE("Server identity verification failure"),
    TWO_FACTOR_REQUIRED(""),
    ACTIVATION_REQUIRED(""),
    LOGGED_OUT(""),
    HOST_UNREACHABLE("Host unreachable"),

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
    LOAD_SETTINGS_FAILURE("Load settings failure"),
    OBSERVE_SETTINGS_FAILURE("Observe settings failure"),
    SAVE_SETTINGS_FAILURE("Save settings failure"),
    STORE_CERTIFICATE_FAILURE("Store certificate failure"),
    STORE_URL_FAILURE("Store url failure"),
    GET_URL_FAILURE("Get url failure"),
    GET_CERTIFICATE_FAILURE("Get certificate failure"),
    OPEN_URI_INPUT_STREAM_FAILURE("Open uri input stream failure"),
    READ_BYTES_FROM_URI_FAILURE("Read bytes from uri failure"),
    CLEAR_CERTIFICATE_FAILURE("Clear certificate failure"),

    // Other errors
    BIOMETRIC("Biometric error"),
    UNKNOWN("Unknown error"),
}