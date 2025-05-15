package com.alexianhentiu.vaultberryapp.domain.utils.validation

import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType

class RegularValidator : InputValidator {

    override fun getValidatorFunction(type: ValidatedFieldType): (String) -> Boolean {
        when (type) {
            ValidatedFieldType.EMAIL -> return ::validateEmail
            ValidatedFieldType.PASSWORD -> return ::validatePassword
            ValidatedFieldType.OTP -> return ::validateOTP
            ValidatedFieldType.MFA_CODE -> return ::validate2FACode
            ValidatedFieldType.ENTRY_TITLE -> return ::validateEntryTitle
            else -> return { false }
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$")
        return email.matches(emailRegex)
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isBlank()) return false
        val minLength = 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return password.length >= minLength && hasUppercase && hasDigit && hasSpecialChar
    }

    private fun validateOTP(otp: String): Boolean {
        if (otp.isBlank()) return false
        val otpRegex = Regex("^\\d{9}$")
        return otp.matches(otpRegex)
    }

    private fun validate2FACode(code: String): Boolean {
        if (code.isBlank()) return false
        val codeRegex = Regex("^\\d{6}$")
        return code.matches(codeRegex)
    }

    private fun validateEntryTitle(title: String): Boolean {
        return title.isNotBlank()
    }
}