package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.AESHandler
import org.junit.Before
import org.junit.Test

class VaultGuardianTest {

    companion object {
        private val handler = AESHandler()
    }

    private lateinit var guardian: VaultGuardian

    @Before
    fun initGuardian() {
        guardian = VaultGuardian(handler)
    }

    @Test
    fun givenNonEmptyPassword_whenGenerateKeyChainWithNonEmptyFields() {
        val keyChain = guardian.generateKeyChain("password", "a")
        assert(keyChain.vaultKey.isNotEmpty())
        assert(keyChain.recoveryKey.isNotEmpty())
        assert(keyChain.salt.isNotEmpty())
    }

    @Test
    fun givenEmptyPasswordOrRecoveryPassword_whenGenerateKeyChain_thenThrowIllegalArgumentException() {
        try {
            guardian.generateKeyChain("", "")
        } catch (e: IllegalArgumentException) {
            assert(e.message == "Password cannot be empty")
        }
    }

    @Test
    fun givenNonEmptyPasswordAndEncryptedVaultKey_whenDecryptVaultKey_thenReturnDecryptedKeyWithNonEmptyKey() {
        val keyChain = guardian.generateKeyChain("password", "a")
        val decryptedVaultKey = guardian.decryptKey("password", keyChain.salt, keyChain.vaultKey)
        assert(decryptedVaultKey.key.isNotEmpty())
    }

    @Test
    fun givenDecryptedVaultKey_whenEncryptField_thenReturnEncryptedVaultField() {
        val keyChain = guardian.generateKeyChain("password", "a")
        val decryptedVaultKey = guardian.decryptKey("password", keyChain.salt, keyChain.vaultKey)
        val encryptedField = guardian.encryptField("test", decryptedVaultKey)
        assert(encryptedField.isNotEmpty())
    }

    @Test
    fun givenEncryptedVaultField_whenDecryptField_thenReturnInitialField() {
        val keyChain = guardian.generateKeyChain("password", "a")
        val decryptedVaultKey = guardian.decryptKey("password", keyChain.salt, keyChain.vaultKey)
        val encryptedField = guardian.encryptField("test", decryptedVaultKey)
        val decryptedField = guardian.decryptField(encryptedField, decryptedVaultKey)
        assert(decryptedField == "test")
    }

}