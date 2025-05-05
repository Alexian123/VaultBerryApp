package com.alexianhentiu.vaultberryapp.domain.utils.validation

interface InputValidator {

    fun validateEmail(email: String): Boolean

    fun validatePassword(password: String): Boolean

    fun validateOTP(otp: String): Boolean

    fun validate2FACode(code: String): Boolean

    fun validateEntryTitle(title: String): Boolean
}