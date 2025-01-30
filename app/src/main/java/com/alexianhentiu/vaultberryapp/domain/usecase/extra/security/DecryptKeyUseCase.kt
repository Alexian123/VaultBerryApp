package com.alexianhentiu.vaultberryapp.domain.usecase.extra.security

import com.alexianhentiu.vaultberryapp.domain.model.DecryptedKey
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian

class DecryptKeyUseCase(private val vaultGuardian: VaultGuardian) {
    operator fun invoke(
        password: String,
        salt: String,
        encryptedKey: String
    ): DecryptedKey {
        return DecryptedKey(
            vaultGuardian.decryptKey(password, salt, encryptedKey)
        )
    }
}