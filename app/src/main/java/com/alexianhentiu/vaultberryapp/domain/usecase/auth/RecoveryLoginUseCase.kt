package com.alexianhentiu.vaultberryapp.domain.usecase.auth

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface RecoveryLoginUseCase {
    suspend operator fun invoke(
        email: String,
        recoveryPassword: String,
        otp: String
    ): UseCaseResult<ByteArray>
}