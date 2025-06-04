package com.alexianhentiu.vaultberryapp.domain.security.password

import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength

interface PasswordEvaluator {
    fun evaluateStrength(password: String): PasswordStrength
}