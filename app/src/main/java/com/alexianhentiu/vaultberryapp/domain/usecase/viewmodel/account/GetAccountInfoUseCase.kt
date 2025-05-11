package com.alexianhentiu.vaultberryapp.domain.usecase.viewmodel.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.UseCaseResult
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ErrorType

class GetAccountInfoUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): UseCaseResult<AccountInfo> {
        return when (val accountResult = accountRepository.getAccountInfo()) {
            is APIResult.Success -> {
                UseCaseResult.Success(accountResult.data)
            }

            is APIResult.Error -> {
                UseCaseResult.Error(
                    ErrorType.EXTERNAL,
                    accountResult.source,
                    accountResult.message
                )
            }
        }
    }
}