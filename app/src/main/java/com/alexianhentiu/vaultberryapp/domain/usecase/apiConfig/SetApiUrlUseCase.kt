package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface SetApiUrlUseCase {
    suspend operator fun invoke(url: String): UseCaseResult<Unit>
}