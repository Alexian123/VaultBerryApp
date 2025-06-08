package com.alexianhentiu.vaultberryapp.domain.model

data class PasswordGenOptions(
    val length: Int = 16,
    val includeUppercase: Boolean = true,
    val includeNumbers: Boolean = true,
    val includeSpecialChars: Boolean = true,
    val includeSpaces: Boolean = false
) {
    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var length: Int = 16
        private var includeUppercase: Boolean = true
        private var includeNumbers: Boolean = true
        private var includeSpecialChars: Boolean = true
        private var includeSpaces: Boolean = false

        fun length(length: Int): Builder = apply { this.length = length }
        fun includeUppercase(include: Boolean): Builder = apply { this.includeUppercase = include }
        fun includeNumbers(include: Boolean): Builder = apply { this.includeNumbers = include }
        fun includeSpecialChars(include: Boolean): Builder =
            apply { this.includeSpecialChars = include }
        fun includeSpaces(include: Boolean): Builder = apply { this.includeSpaces = include }

        fun build(): PasswordGenOptions {
            return PasswordGenOptions(
                length = length,
                includeUppercase = includeUppercase,
                includeNumbers = includeNumbers,
                includeSpecialChars = includeSpecialChars,
                includeSpaces = includeSpaces
            )
        }
    }
}