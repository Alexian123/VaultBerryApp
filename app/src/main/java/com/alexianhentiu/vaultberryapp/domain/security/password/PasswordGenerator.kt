package com.alexianhentiu.vaultberryapp.domain.security.password

import com.alexianhentiu.vaultberryapp.domain.common.PasswordGenOptions

interface PasswordGenerator {
    fun generate(options: PasswordGenOptions): String
}