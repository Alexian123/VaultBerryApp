package com.alexianhentiu.vaultberryapp.utils

import com.alexianhentiu.vaultberryapp.data.security.AESHandler
import com.alexianhentiu.vaultberryapp.data.security.PBKDF2SHA256Handler
import com.alexianhentiu.vaultberryapp.data.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.security.GeneralCryptoHandler
import com.alexianhentiu.vaultberryapp.domain.security.KeyDerivationHandler
import com.alexianhentiu.vaultberryapp.domain.security.VaultSecurityHandler
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import org.junit.Before
import org.junit.Test
import java.util.Base64

class VaultGuardianTest {

    private object StubBase64Handler : Base64Handler {
        override fun encode(bytes: ByteArray): String {
            return Base64.getEncoder().encodeToString(bytes)
        }

        override fun decode(encoded: String): ByteArray {
            return Base64.getDecoder().decode(encoded)
        }

    }

    private object StubStringResourceProvider : StringResourceProvider {
        override fun getString(resId: Int): String {
            return "Password cannot be empty"
        }

        override fun getString(resId: Int, vararg args: Any): String {
            return ""
        }
    }

    private lateinit var cryptoHandler: GeneralCryptoHandler
    private lateinit var keyDerivationHandler: KeyDerivationHandler
    private lateinit var guardian: VaultSecurityHandler

    @Before
    fun initGuardian() {
        cryptoHandler = AESHandler()
        keyDerivationHandler = PBKDF2SHA256Handler(StubStringResourceProvider)
        guardian = VaultGuardian(StubBase64Handler, cryptoHandler, keyDerivationHandler)
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
        assert(decryptedVaultKey.isNotEmpty())
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