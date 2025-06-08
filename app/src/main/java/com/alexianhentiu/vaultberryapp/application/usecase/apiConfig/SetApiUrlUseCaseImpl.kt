package com.alexianhentiu.vaultberryapp.application.usecase.apiConfig

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.apiConfig.SetApiUrlUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class SetApiUrlUseCaseImpl(
    private val apiConfigRepository: ApiConfigRepository,
    private val stringResourceProvider: StringResourceProvider
) : SetApiUrlUseCase {
    override suspend fun invoke(url: String): UseCaseResult<Unit> {
        try {
            apiConfigRepository.storeUrl(url)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.STORE_URL_FAILURE,
                    stringResourceProvider.getString(R.string.api_config_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }
}