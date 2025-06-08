package com.alexianhentiu.vaultberryapp.data.repository.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.ApiPreferencesDataStoreQualifier
import com.alexianhentiu.vaultberryapp.domain.repository.ApiConfigRepository
import com.alexianhentiu.vaultberryapp.domain.utils.Base64Handler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreApiConfigRepository @Inject constructor(
    @ApiPreferencesDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val base64Handler: Base64Handler
) : ApiConfigRepository {

    private val urlKey = stringPreferencesKey("url")
    private val certificateKey = stringPreferencesKey("certificate")

    override suspend fun getUrl(): String? {
        return dataStore.data.map { preferences ->
            preferences[urlKey]
        }.first()
    }

    override suspend fun storeUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[urlKey] = url
        }
    }

    override suspend fun getCertificateBytes(): ByteArray? {
        return dataStore.data.map { preferences ->
            val certificate = preferences[certificateKey]
            if (certificate != null) {
                base64Handler.decode(certificate)
            }
            else null
        }.first()
    }

    override suspend fun storeCertificateBytes(certificateBytes: ByteArray) {
        dataStore.edit { preferences ->
            preferences[certificateKey] = base64Handler.encode(certificateBytes)
        }
    }

    override suspend fun clearCertificate() {
        dataStore.edit { preferences ->
            preferences.remove(certificateKey)
        }
    }
}