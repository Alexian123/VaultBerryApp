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
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
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
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase,
        stringResourceProvider: StringResourceProvider
    ): AddEntryUseCase = AddEntryUseCaseImpl(
        vaultRepository,
        encryptVaultEntryUseCase,
        stringResourceProvider
    )

    @Provides
    fun provideDeleteEntryUseCase(
        vaultRepository: VaultRepository,
        stringResourceProvider: StringResourceProvider
    ): DeleteEntryUseCase = DeleteEntryUseCaseImpl(
        vaultRepository,
        stringResourceProvider
    )

    @Provides
    fun provideGetAllVaultEntryPreviewsUseCase(
        vaultRepository: VaultRepository,
        stringResourceProvider: StringResourceProvider
    ): GetAllVaultEntryPreviewsUseCase = GetAllVaultEntryPreviewsUseCaseImpl(
        vaultRepository,
        stringResourceProvider
    )

    @Provides
    fun getDecryptedVaultEntryUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
        stringResourceProvider: StringResourceProvider
    ): GetDecryptedVaultEntryUseCase = GetDecryptedVaultEntryUseCaseImpl(
        vaultRepository,
        decryptVaultEntryUseCase,
        stringResourceProvider
    )

    @Provides
    fun provideSearchVaultEntriesUseCase(
        vaultRepository: VaultRepository,
        decryptVaultEntryUseCase: DecryptVaultEntryUseCase,
        stringResourceProvider: StringResourceProvider
    ): SearchVaultEntriesUseCase = SearchVaultEntriesUseCaseImpl(
        vaultRepository,
        decryptVaultEntryUseCase,
        stringResourceProvider
    )

    @Provides
    fun provideUpdateEntryUseCase(
        vaultRepository: VaultRepository,
        encryptVaultEntryUseCase: EncryptVaultEntryUseCase,
        stringResourceProvider: StringResourceProvider
    ): UpdateEntryUseCase = UpdateEntryUseCaseImpl(
        vaultRepository,
        encryptVaultEntryUseCase,
        stringResourceProvider
    )
}