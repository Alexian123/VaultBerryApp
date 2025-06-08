package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface GetApiCertificateUseCase {
    suspend operator fun invoke(): UseCaseResult<ByteArray?>
}