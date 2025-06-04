package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.internal.DecryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.internal.EncryptVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.application.usecase.vault.AddEntryUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.vault.DeleteEntryUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.vault.GetAllVaultEntryPreviewsUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.vault.GetDecryptedVaultEntryUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.vault.SearchVaultEntriesUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.vault.UpdateEntryUseCaseImpl
import com.alexianhentiu.vaultberryapp.domain.repository.VaultRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.DeleteEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetAllVaultEntryPreviewsUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.GetDecryptedVaultEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.SearchVaultEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.vault.UpdateEntryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object VaultUseCaseModule {

    @Provides
    fun provideAddEntryUseCase(
        vaultRepository: VaultRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): AddEntryUseCase = AddEntryUseCaseImpl(vaultRepository, encryptVaultEntryUseCase)

    @Provides
    fun provideDeleteEntryUseCase(
        vaultRepository: VaultRepository
    ): DeleteEntryUseCase = DeleteEntryUseCaseImpl(vaultRepository)

    @Provides
    fun provideGetAllVaultEntryPreviewsUseCase(
        vaultRepository: VaultRepository,
    ): GetAllVaultEntryPreviewsUseCase = GetAllVaultEntryPreviewsUseCaseImpl(vaultRepository)

    @Provides
    fun getDecryptedVaultEntryUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase
    ): GetDecryptedVaultEntryUseCase = GetDecryptedVaultEntryUseCaseImpl(
        vaultRepository,
        decryptVaultEntryUseCase
    )

    @Provides
    fun provideSearchVaultEntriesUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase
    ): SearchVaultEntriesUseCase = SearchVaultEntriesUseCaseImpl(
        vaultRepository,
        decryptVaultEntryUseCase
    )

    @Provides
    fun provideUpdateEntryUseCase(
        vaultRepository: VaultRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase
    ): UpdateEntryUseCase = UpdateEntryUseCaseImpl(vaultRepository, encryptVaultEntryUseCase)
}