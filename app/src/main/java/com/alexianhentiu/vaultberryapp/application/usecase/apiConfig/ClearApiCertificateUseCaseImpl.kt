package com.alexianhentiu.vaultberryapp.application.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.ClearApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class ClearApiCertificateUseCaseImpl(
    private val apiConfigRepository: ApiConfigRepository,
    private val stringResourceProvider: StringResourceProvider
) : ClearApiCertificateUseCase {

    override suspend fun invoke(): UseCaseResult<Unit> {
        try {
            apiConfigRepository.clearCertificate()
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorType.CLEAR_CERTIFICATE_FAILURE,
                stringResourceProvider.getString(R.string.api_config_error_source),
                e.message ?: stringResourceProvider.getString(R.string.unknown_error)
            )
        }
    }

}