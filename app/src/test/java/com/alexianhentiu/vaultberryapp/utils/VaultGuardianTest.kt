package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.AESHandler
import org.junit.Before
import org.junit.Test

class VaultGuardianTest {

    companion object {
        private val handler = AESHandler("CBC/PKCS5Padding")
    }

    private lateinit var guardian: VaultGuardian

    @Before
    fun initGuardian() {
        guardian = VaultGuardian(handler)
    }

    @Test
    fun givenNonEmptyPassword_whenExportNewVaultKey_thenReturnEncryptedVaultKeyWithNonEmptyFields() {
        val encryptedVaultKey = guardian.exportNewVaultKey("password")
        assert(encryptedVaultKey.ivAndKey.isNotEmpty())
        assert(encryptedVaultKey.salt.isNotEmpty())
    }

    @Test
    fun givenEmptyPassword_whenExportNewVaultKey_thenThrowIllegalArgumentException() {
        try {
            guardian.exportNewVaultKey("")
        } catch (e: IllegalArgumentException) {
            assert(e.message == "Password cannot be empty")
        }
    }

    @Test
    fun givenNonEmptyPasswordAndEncryptedVaultKey_whenImportExistingVaultKey_thenReturnDecryptedVaultKeyWithNonEmptyKey() {
        val encryptedVaultKey = guardian.exportNewVaultKey("password")
        val decryptedVaultKey = guardian.importExistingVaultKey("password", encryptedVaultKey)
        assert(decryptedVaultKey.key.isNotEmpty())
    }

    @Test
    fun givenDecryptedVaultKey_whenExportField_thenReturnEncryptedVaultField() {
        val encryptedVaultKey = guardian.exportNewVaultKey("password")
        val decryptedVaultKey = guardian.importExistingVaultKey("password", encryptedVaultKey)
        val encryptedField = guardian.exportField("test", decryptedVaultKey)
        assert(encryptedField.isNotEmpty())
    }

    @Test
    fun givenEncryptedVaultField_whenImportField_thenReturnInitialField() {
        val encryptedVaultKey = guardian.exportNewVaultKey("password")
        val decryptedVaultKey = guardian.importExistingVaultKey("password", encryptedVaultKey)
        val encryptedField = guardian.exportField("test", decryptedVaultKey)
        val decryptedField = guardian.importField(encryptedField, decryptedVaultKey)
        assert(decryptedField == "test")
    }

}