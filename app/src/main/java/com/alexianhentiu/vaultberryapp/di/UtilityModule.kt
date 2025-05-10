package com.alexianhentiu.vaultberryapp.di

import android.content.ClipboardManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.domain.utils.security.AuthGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.AESHandler
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.CryptographyHandler
import com.alexianhentiu.vaultberryapp.domain.utils.validation.DebugValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.RegularValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {

    // Set up DataStore
    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "com.alexianhentiu.vaultberryapp.user_settings"
    )

    @Provides
    @Singleton
    fun provideClipboard(
        @ApplicationContext context: Context,
    ): ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    @Provides
    @Singleton
    fun provideCryptoHandler(): CryptographyHandler = AESHandler()

    @Provides
    @Singleton
    fun providePasswordGenerator(): PasswordGenerator = PasswordGenerator()

    @Provides
    @Singleton
    fun providePasswordEvaluator(): PasswordEvaluator = PasswordEvaluator()

    @Provides
    @Singleton
    fun provideInputValidator(
        dataStore: DataStore<Preferences>
    ): InputValidator {
        val isDebugModeEnabled = runBlocking {
            dataStore.data.first()[booleanPreferencesKey("debug_mode")] == true
        }
        return if (isDebugModeEnabled) {
            DebugValidator()
        } else {
            RegularValidator()
        }
    }

    @Provides
    @Singleton
    fun provideVaultGuardian(
        cryptoHandler: CryptographyHandler
    ): VaultGuardian = VaultGuardian(cryptoHandler)

    @Provides
    @Singleton
    fun provideAuthGuardian(): AuthGuardian = AuthGuardian()

    @Singleton
    @Provides
    fun providesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.userDataStore

    @Provides
    @Singleton
    fun provideModelConverter(): ModelConverter = ModelConverter()

    @Provides
    @Singleton
    fun provideAPIResponseHandler(): APIResponseHandler = APIResponseHandler()
}