package com.alexianhentiu.vaultberryapp.application.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.GetApiCertificateUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class GetApiCertificateUseCaseImpl(
    private val apiConfigRepository: ApiConfigRepository,
    private val stringResourceProvider: StringResourceProvider
) : GetApiCertificateUseCase {

    override suspend fun invoke(): UseCaseResult<ByteArray?> {
        try {
            val certificate = apiConfigRepository.getCertificateBytes()
            return UseCaseResult.Success(certificate)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.GET_CERTIFICATE_FAILURE,
                    stringResourceProvider.getString(R.string.api_config_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }

}