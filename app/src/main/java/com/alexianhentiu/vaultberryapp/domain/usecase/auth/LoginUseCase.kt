package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface LoginUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        totpCode: String? = null
    ): UseCaseResult<ByteArray>
}