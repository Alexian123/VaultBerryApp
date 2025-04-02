package com.alexianhentiu.vaultberryapp.domain.usecase.general.account

import com.alexianhentiu.vaultberryapp.data.utils.APIResult
import com.alexianhentiu.vaultberryapp.domain.model.entity.AccountInfo
import com.alexianhentiu.vaultberryapp.domain.repository.AccountRepository
import com.alexianhentiu.vaultberryapp.domain.utils.types.ActionResult
import com.alexianhentiu.vaultberryapp.domain.utils.types.ErrorType

class GetAccountInfoUseCase(private val accountRepository: AccountRepository) {

    suspend operator fun invoke(): ActionResult<AccountInfo> {
        return when (val accountResult = accountRepository.getAccountInfo()) {
            is APIResult.Success -> {
                ActionResult.Success(accountResult.data)
            }

            is APIResult.Error -> {
                ActionResult.Error(
                    ErrorType.EXTERNAL,
                    accountResult.source,
                    accountResult.message
                )
            }
        }
    }
}