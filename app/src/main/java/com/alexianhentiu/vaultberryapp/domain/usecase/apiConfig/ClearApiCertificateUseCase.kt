package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface ClearApiCertificateUseCase {
    suspend operator fun invoke(): UseCaseResult<Unit>
}