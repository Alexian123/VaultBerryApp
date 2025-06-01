package com.alexianhentiu.vaultberryapp.di.modules

import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alexianhentiu.vaultberryapp.data.datastore.userDataStore
import com.alexianhentiu.vaultberryapp.data.utils.APIResponseHandler
import com.alexianhentiu.vaultberryapp.data.utils.ModelConverter
import com.alexianhentiu.vaultberryapp.di.qualifiers.DebugValidatorQualifier
import com.alexianhentiu.vaultberryapp.di.qualifiers.RegularValidatorQualifier
import com.alexianhentiu.vaultberryapp.domain.utils.security.AuthGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.utils.security.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.AESHandler
import com.alexianhentiu.vaultberryapp.domain.utils.security.cryptography.CryptographyHandler
import com.alexianhentiu.vaultberryapp.domain.utils.validation.DebugValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.RegularValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilityModule {

    @Provides
    @Singleton
    fun provideCredentialsSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(
        "auth_credentials",
        Context.MODE_PRIVATE
    )

    @Provides
    @Singleton
    fun provideClipboardManager(
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
    @DebugValidatorQualifier
    fun provideDebugInputValidator(): InputValidator {
        return DebugValidator()
    }

    @Provides
    @Singleton
    @RegularValidatorQualifier
    fun provideRegularInputValidator(): InputValidator {
        return RegularValidator()
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