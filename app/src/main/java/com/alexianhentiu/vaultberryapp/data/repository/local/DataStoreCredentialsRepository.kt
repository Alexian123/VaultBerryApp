package com.alexianhentiu.vaultberryapp.data.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.AuthCredentialsDataStoreQualifier
import com.alexianhentiu.vaultberryapp.domain.model.EncryptedAuthCredentials
import com.alexianhentiu.vaultberryapp.domain.repository.CredentialsRepository
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreCredentialsRepository @Inject constructor(
    @AuthCredentialsDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val base64Handler: Base64Handler
) : CredentialsRepository {

    private val encryptedPasswordKey = stringPreferencesKey("encrypted_password")
    private val ivKey = stringPreferencesKey("iv")
    private val emailKey = stringPreferencesKey("email")

    override suspend fun storeCredentials(encryptedCredentials: EncryptedAuthCredentials) {
        dataStore.edit { preferences ->
            preferences[encryptedPasswordKey] = base64Handler.encode(
                encryptedCredentials.encryptedPassword
            )
            preferences[ivKey] = base64Handler.encode(
                encryptedCredentials.passwordIv
            )
            preferences[emailKey] = encryptedCredentials.email
        }
    }

    override suspend fun storeEncryptedPassword(encryptedData: ByteArray, iv: ByteArray) {
        dataStore.edit { preferences ->
            preferences[encryptedPasswordKey] = base64Handler.encode(encryptedData)
            preferences[ivKey] = base64Handler.encode(iv)
        }
    }

    override suspend fun storeEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    override suspend fun getCredentials(): EncryptedAuthCredentials? {
        return dataStore.data.map { preferences ->
            val encryptedPassword = preferences[encryptedPasswordKey]
            val iv = preferences[ivKey]
            val email = preferences[emailKey]

            if (encryptedPassword != null && iv != null && email != null) {
                EncryptedAuthCredentials(
                    encryptedPassword = base64Handler.decode(encryptedPassword),
                    passwordIv = base64Handler.decode(iv),
                    email = email
                )
            } else null
        }.first()
    }

    override suspend fun hasStoredCredentials(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[encryptedPasswordKey] != null &&
                    preferences[ivKey] != null &&
                    preferences[emailKey] != null
        }.first()
    }

    override suspend fun clearStoredCredentials() {
        dataStore.edit { preferences ->
            preferences.remove(encryptedPasswordKey)
            preferences.remove(ivKey)
            preferences.remove(emailKey)
        }
    }
}