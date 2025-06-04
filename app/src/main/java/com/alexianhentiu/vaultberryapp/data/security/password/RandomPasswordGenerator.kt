package com.alexianhentiu.vaultberryapp.data.security.password

import com.alexianhentiu.vaultberryapp.domain.common.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator

class RandomPasswordGenerator : PasswordGenerator {

    override fun generate(options: PasswordGenOptions): String {
        if (options.length <= 0) throw IllegalArgumentException("Length must be greater than 0")
        val charPool = mutableListOf<Char>()
        charPool.addAll(('a'..'z').toList())
        if (options.includeUppercase) charPool.addAll(('A'..'Z').toList())
        if (options.includeNumbers) charPool.addAll(('0'..'9').toList())
        if (options.includeSpecialChars) charPool.addAll("!@#$%^&*()_+{}|;:,.<>?".toList())
        if (options.includeSpaces) charPool.add(' ')
        return (1..options.length)
            .map { charPool.random() }
            .joinToString("")
    }
}