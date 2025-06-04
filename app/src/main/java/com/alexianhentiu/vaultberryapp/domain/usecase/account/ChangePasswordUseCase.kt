package com.alexianhentiu.vaultberryapp.domain.usecase.account

import com.alexianhentiu.vaultberryapp.domain.common.UseCaseResult

interface ChangePasswordUseCase {
    suspend operator fun invoke(
        decryptedKey: ByteArray,
        newPassword: String,
        reEncrypt: Boolean = false
    ): UseCaseResult<String>
}