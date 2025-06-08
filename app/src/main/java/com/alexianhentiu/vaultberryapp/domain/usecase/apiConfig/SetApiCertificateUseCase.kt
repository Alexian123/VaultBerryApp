package com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface SetApiCertificateUseCase {
    suspend operator fun invoke(certificateBytes: ByteArray): UseCaseResult<Unit>
}