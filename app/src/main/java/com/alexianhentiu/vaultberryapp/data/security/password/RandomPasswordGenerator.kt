package com.alexianhentiu.vaultberryapp.data.security.password

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.model.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class RandomPasswordGenerator(
    private val stringResourceProvider: StringResourceProvider
) : PasswordGenerator {

    override fun generate(options: PasswordGenOptions): String {
        if (options.length <= 0) throw IllegalArgumentException(
            stringResourceProvider.getString(R.string.error_password_gen_invalid_length)
        )
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