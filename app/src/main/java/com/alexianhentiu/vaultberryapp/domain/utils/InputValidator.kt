package com.alexianhentiu.vaultberryapp.domain.utils

class InputValidator {

    fun validateEmail(email: String): Boolean {
        if (email.isBlank()) return false
        /* TODO: uncomment after testing
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return email.matches(emailRegex)
         */
        return true
    }

    fun validatePassword(password: String): Boolean {
        if (password.isBlank()) return false

        /* TODO: uncomment after testing
        val minLength = 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() } // Simplified check for any non-letter/digit

        return password.length >= minLength && hasUppercase && hasDigit && hasSpecialChar
         */
        return true
    }

    fun validateOTP(otp: String): Boolean {
        if (otp.isBlank()) return false
        val otpRegex = Regex("^\\d{9}\$")
        return otp.matches(otpRegex)
    }

    fun validate2FACode(code: String): Boolean {
        if (code.isBlank()) return false
        val codeRegex = Regex("^\\d{6}\$")
        return code.matches(codeRegex)
    }

    fun validateEntryTitle(title: String): Boolean {
        return title.isNotBlank()
    }
}