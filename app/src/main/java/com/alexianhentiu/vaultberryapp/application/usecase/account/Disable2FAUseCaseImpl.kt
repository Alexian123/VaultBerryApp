package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.Disable2FAUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class Disable2FAUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val stringResourceProvider: StringResourceProvider
) : Disable2FAUseCase {

    override suspend operator fun invoke(): UseCaseResult<MessageResponse> {
        return try {
            when (val result = accountRepository.disable2FA()) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(result.data)
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            result.source,
                            result.message
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