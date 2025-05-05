package com.alexianhentiu.vaultberryapp.domain.utils.validation

class RegularValidator : InputValidator {

    override fun validateEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$")
        return email.matches(emailRegex)
    }

    override fun validatePassword(password: String): Boolean {
        if (password.isBlank()) return false
        val minLength = 8
        val hasUppercase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        return password.length >= minLength && hasUppercase && hasDigit && hasSpecialChar
    }

    override fun validateOTP(otp: String): Boolean {
        if (otp.isBlank()) return false
        val otpRegex = Regex("^\\d{9}$")
        return otp.matches(otpRegex)
    }

    override fun validate2FACode(code: String): Boolean {
        if (code.isBlank()) return false
        val codeRegex = Regex("^\\d{6}$")
        return code.matches(codeRegex)
    }

    override fun validateEntryTitle(title: String): Boolean {
        return title.isNotBlank()
    }

}