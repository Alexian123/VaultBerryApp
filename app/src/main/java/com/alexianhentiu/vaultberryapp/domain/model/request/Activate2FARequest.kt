package com.alexianhentiu.vaultberryapp.domain.model.request

data class Activate2FARequest(
    val totpCode: String
)