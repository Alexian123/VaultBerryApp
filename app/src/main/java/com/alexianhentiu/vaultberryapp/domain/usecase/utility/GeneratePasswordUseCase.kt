package com.alexianhentiu.vaultberryapp.domain.usecase.utility

import com.alexianhentiu.vaultberryapp.domain.model.PasswordGenOptions
import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface GeneratePasswordUseCase {
    operator fun invoke(options: PasswordGenOptions): UseCaseResult<String>
}