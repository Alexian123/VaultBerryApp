package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface PingApiUseCase {
    suspend operator fun invoke(
        host: String,
        port: Int,
        timeoutMs: Int
    ): UseCaseResult<Unit>
}