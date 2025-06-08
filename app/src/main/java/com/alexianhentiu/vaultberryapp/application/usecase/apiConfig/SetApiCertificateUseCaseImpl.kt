package com.alexianhentiu.vaultberryapp.application.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class SetApiCertificateUseCaseImpl(
    private val apiConfigRepository: ApiConfigRepository,
    private val stringResourceProvider: StringResourceProvider
) : SetApiCertificateUseCase {
    override suspend fun invoke(certificateBytes: ByteArray): UseCaseResult<Unit> {
        try {
            apiConfigRepository.storeCertificateBytes(certificateBytes)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.STORE_CERTIFICATE_FAILURE,
                stringResourceProvider.getString(R.string.api_config_error_source),
                e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }
}