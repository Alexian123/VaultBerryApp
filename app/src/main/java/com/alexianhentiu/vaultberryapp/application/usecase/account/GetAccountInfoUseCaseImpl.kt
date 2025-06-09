package com.alexianhentiu.vaultberryapp.application.usecase.account

import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.data.remote.ApiResult
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.model.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.common.enums.ErrorType
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo
import com.alexianhentiu.vaultberryapp.domain.usecase.account.GetAccountInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider

class GetAccountInfoUseCaseImpl(
    private val accountRepository: AccountRepository,
    private val stringResourceProvider: StringResourceProvider
) : GetAccountInfoUseCase {

    override suspend operator fun invoke(): UseCaseResult<AccountInfo> {
        return try {
            when (val accountResult = accountRepository.getAccountInfo()) {
                is ApiResult.Success -> {
                    UseCaseResult.Success(accountResult.data)
                }

                is ApiResult.Error -> {
                    UseCaseResult.Error(
                        ErrorInfo(
                            ErrorType.API,
                            accountResult.source,
                            accountResult.message
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