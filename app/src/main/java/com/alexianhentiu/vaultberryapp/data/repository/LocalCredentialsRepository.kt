package com.alexianhentiu.vaultberryapp.data.repository

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository

class LocalCredentialsRepository(
    private val sharedPreferences: SharedPreferences
): CredentialsRepository {

    companion object {
        private const val KEY_ENCRYPTED_PASSWORD = "encrypted_password"
        private const val KEY_IV = "encryption_iv"
        private const val KEY_EMAIL = "stored_email"
    }

    override fun storeCredentials(encryptedCredentials: EncryptedAuthCredentials) {
        sharedPreferences.edit(commit = true) {
            putString(
                KEY_ENCRYPTED_PASSWORD,
                Base64.encodeToString(encryptedCredentials.encryptedPassword, Base64.DEFAULT)
            )
                .putString(KEY_IV, Base64.encodeToString(encryptedCredentials.passwordIv, Base64.DEFAULT))
                .putString(KEY_EMAIL, encryptedCredentials.email)
        }
    }

    override fun storeEncryptedPassword(encryptedData: ByteArray, iv: ByteArray) {
        sharedPreferences.edit(commit = true) {
            putString(
                KEY_ENCRYPTED_PASSWORD,
                Base64.encodeToString(encryptedData, Base64.DEFAULT)
            )
                .putString(KEY_IV, Base64.encodeToString(iv, Base64.DEFAULT))
        }
    }

    override fun storeEmail(email: String) {
        sharedPreferences.edit(commit = true) {
            putString(KEY_EMAIL, email)
        }
    }

    override fun getCredentials(): EncryptedAuthCredentials? {
        val encryptedPassword = sharedPreferences.getString(KEY_ENCRYPTED_PASSWORD, null)
        val passwordIv = sharedPreferences.getString(KEY_IV, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)

        return if (encryptedPassword != null && passwordIv != null && email != null) {
            EncryptedAuthCredentials(
                encryptedPassword = Base64.decode(encryptedPassword, Base64.DEFAULT),
                passwordIv = Base64.decode(passwordIv, Base64.DEFAULT),
                email = email
            )
        } else {
            null
        }
    }

    override fun hasStoredCredentials(): Boolean {
        return sharedPreferences.contains(KEY_ENCRYPTED_PASSWORD) &&
                sharedPreferences.contains(KEY_EMAIL)
    }

    override fun clearStoredCredentials() {
        sharedPreferences.edit(commit = true) {
            remove(KEY_ENCRYPTED_PASSWORD)
                .remove(KEY_IV)
                .remove(KEY_EMAIL)
        }
    }
}