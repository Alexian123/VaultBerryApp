package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.model.request.Activate2FARequest
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Activate2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class Activate2FAUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val stringResourceProvider: StringResourceProvider
) : Activate2FAUseCase {

    override suspend fun invoke(totpCode: String): UseCaseResult<MessageResponse> {
        return try {
            val request = Activate2FARequest(totpCode)
            when (val response = accountRepository.activate2FA(request)) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(response.data)
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            response.source,
                            response.message
                        )
                    )
                }
            }
        } catch (e: Exception) {
            UseCaseResult.Error(
                ErrorInfo(
                    ErrorType.UNKNOWN,
                    stringResourceProvider.getString(R.string.unknown_error_source),
                    e.message ?: stringResourceProvider.getString(R.string.unknown_error)
                )
            )
        }
    }

}