package com.alexianhentiu.vaultberryapp.domain.utils.validation

class DebugValidator : InputValidator {

    override fun validateEmail(email: String): Boolean {
        return email.isNotBlank()
    }

    override fun validatePassword(password: String): Boolean {
        return password.isNotBlank()
    }

    override fun validateOTP(otp: String): Boolean {
        return otp.isNotBlank()
    }

    override fun validate2FACode(code: String): Boolean {
        return code.isNotBlank()
    }

    override fun validateEntryTitle(title: String): Boolean {
        return title.isNotBlank()
    }
}