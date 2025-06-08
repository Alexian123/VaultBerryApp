package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface GetApiUrlUseCase {
    suspend operator fun invoke(): UseCaseResult<String>
}