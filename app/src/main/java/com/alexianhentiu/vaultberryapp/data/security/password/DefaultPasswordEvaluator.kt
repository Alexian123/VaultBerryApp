package com.alexianhentiu.vaultberryapp.data.security.password

import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordEvaluator

class DefaultPasswordEvaluator : PasswordEvaluator {

    override fun evaluateStrength(password: String): PasswordStrength {
        if (password.isEmpty()) {
            return PasswordStrength.NONE
        }

        var score = 0

        if (password.length >= 8) {
            score++
        }
        if (password.matches(Regex(".*[A-Z].*"))) {
            score++
        }
        if (password.matches(Regex(".*[a-z].*"))) {
            score++
        }
        if (password.matches(Regex(".*[0-9].*"))) {
            score++
        }
        if (password.matches(Regex(".*[!@#$%^&*()-+_=].*"))) {
            score++
        }

        return when (score) {
            0, 1 -> PasswordStrength.WEAK
            2, 3 -> PasswordStrength.AVERAGE
            4 -> PasswordStrength.STRONG
            5 -> PasswordStrength.VERY_STRONG
            else -> PasswordStrength.NONE
        }
    }
}