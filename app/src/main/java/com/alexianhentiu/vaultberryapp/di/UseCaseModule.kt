package com.alexianhentiu.vaultberryapp.di


import com.alexianhentiu.vaultberryapp.domain.repository.UserRepository
import com.alexianhentiu.vaultberryapp.domain.repository.VaultEntryRepository
import com.alexianhentiu.vaultberryapp.domain.usecase.AddEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.GetEntriesUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.KeyExportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.KeyImportUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.LoginUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.LogoutUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.ModifyEntryUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.RegisterUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.RemoveEntryUseCase
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
    fun provideModifyEntryUseCase(vaultEntryRepository: VaultEntryRepository): ModifyEntryUseCase = ModifyEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideRemoveEntryUseCase(vaultEntryRepository: VaultEntryRepository): RemoveEntryUseCase = RemoveEntryUseCase(vaultEntryRepository)

    @Provides
    fun provideKeyExportUseCase(vaultGuardian: VaultGuardian): KeyExportUseCase = KeyExportUseCase(vaultGuardian)

    @Provides
    fun provideKeyImportUseCase(vaultGuardian: VaultGuardian): KeyImportUseCase = KeyImportUseCase(vaultGuardian)

}