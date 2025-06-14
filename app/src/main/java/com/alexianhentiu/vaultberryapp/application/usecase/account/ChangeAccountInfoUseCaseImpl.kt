package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.model.response.MessageResponse
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.ChangeAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class ChangeAccountInfoUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val stringResourceProvider: StringResourceProvider
) : ChangeAccountInfoUseCase {
    override suspend operator fun invoke(
        accountInfo: AccountInfo,
        noActivation: Boolean
    ): UseCaseResult<MessageResponse> {
        return try {
            when (val updateResult = accountRepository.updateAccount(accountInfo, noActivation)) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(updateResult.data)
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            updateResult.source,
                            updateResult.message
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