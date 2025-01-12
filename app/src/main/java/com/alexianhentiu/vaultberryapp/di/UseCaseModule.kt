package com.alexianhentiu.vaultberryapp.di


import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.KeyExportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.KeyImportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.auth.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideRegisterUseCase(userRepository: UserRepository): RegisterUseCase = RegisterUseCase(userRepository)

    @Provides
    fun provideLoginUseCase(userRepository: UserRepository): LoginUseCase = LoginUseCase(userRepository)

    @Provides
    fun provideLogoutUseCase(userRepository: UserRepository): LogoutUseCase = LogoutUseCase(userRepository)

    @Provides
    fun provideGetEntriesUseCase(vaultEntryRepository: VaultEntryRepository): GetEntriesUseCase = GetEntriesUseCase(vaultEntryRepository)

    @Provides
    fun provideAddEntryUseCase(vaultEntryRepository: VaultEntryRepository): AddEntryUseCase = AddEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideUpdateEntryUseCase(vaultEntryRepository: VaultEntryRepository): UpdateEntryUseCase = UpdateEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideDeleteEntryUseCase(vaultEntryRepository: VaultEntryRepository): DeleteEntryUseCase = DeleteEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideKeyExportUseCase(vaultGuardian: VaultGuardian): KeyExportUseCase = KeyExportUseCase(vaultGuardian)

    @Provides
    fun provideKeyImportUseCase(vaultGuardian: VaultGuardian): KeyImportUseCase = KeyImportUseCase(vaultGuardian)

    @Provides
    fun provideDecryptVaultEntryUseCase(vaultGuardian: VaultGuardian): DecryptVaultEntryUseCase = DecryptVaultEntryUseCase(vaultGuardian)

    @Provides
    fun provideEncryptVaultEntryUseCase(vaultGuardian: VaultGuardian): EncryptVaultEntryUseCase = EncryptVaultEntryUseCase(vaultGuardian)
}