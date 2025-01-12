package com.alexianhentiu.vaultberryapp.domain.utils

class InputValidator {

    fun validateEmail(email: String): Boolean {
        if (email.isBlank()) return false
        /* TODO("Uncomment after testing")
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return email.matches(emailRegex)*/
        return true;
    }

    fun validatePassword(password: String): Boolean {
        if (password.isBlank()) return false
        // TODO("Implement password validation")
        return true
    }

    fun validateEntryTitle(title: String): Boolean {
        return title.isNotBlank()
    }
}