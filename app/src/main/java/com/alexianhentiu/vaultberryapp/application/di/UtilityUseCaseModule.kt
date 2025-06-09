package com.alexianhentiu.vaultberryapp.application.di

import com.alexianhentiu.vaultberryapp.application.usecase.utility.CopyToClipboardUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.utility.EvalPasswordStrengthUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.utility.GeneratePasswordUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.utility.GetAppInfoUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.utility.GetValidatorFunctionUseCaseImpl
import com.alexianhentiu.vaultberryapp.application.usecase.utility.ReadBytesFromUriUseCaseImpl
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.LaxInputValidatorQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.StrictInputValidatorQualifier
import com.alexianhentiu.vaultberryapp.domain.clipboard.ClipboardHandler
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.security.password.PasswordGenerator
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.CopyToClipboardUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.EvalPasswordStrengthUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GeneratePasswordUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetAppInfoUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.GetValidatorFunctionUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.utility.ReadBytesFromUriUseCase
import com.alexianhentiu.vaultberryapp.domain.utils.AppInfoProvider
import com.alexianhentiu.vaultberryapp.domain.utils.StringResourceProvider
import com.alexianhentiu.vaultberryapp.domain.utils.UriStreamProvider
import com.alexianhentiu.vaultberryapp.domain.validation.InputValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UtilityUseCaseModule {

    @Provides
    fun provideCopyToClipboardUseCase(
        stringResourceProvider: StringResourceProvider,
        clipboardHandler: ClipboardHandler
    ): CopyToClipboardUseCase = CopyToClipboardUseCaseImpl(
        stringResourceProvider,
        clipboardHandler
    )

    @Provides
    fun provideEvalPasswordStrengthUseCase(
        stringResourceProvider: StringResourceProvider,
        passwordEvaluator: PasswordEvaluator
    ): EvalPasswordStrengthUseCase = EvalPasswordStrengthUseCaseImpl(
        stringResourceProvider,
        passwordEvaluator
    )

    @Provides
    fun provideGeneratePasswordUseCase(
        stringResourceProvider: StringResourceProvider,
        passwordGenerator: PasswordGenerator
    ): GeneratePasswordUseCase = GeneratePasswordUseCaseImpl(
        stringResourceProvider,
        passwordGenerator
    )

    @Provides
    fun provideGetValidatorFunctionUseCase(
        @LaxInputValidatorQualifier laxValidator: InputValidator,
        @StrictInputValidatorQualifier strictValidator: InputValidator,
        stringResourceProvider: StringResourceProvider
    ): GetValidatorFunctionUseCase = GetValidatorFunctionUseCaseImpl(
        laxValidator,
        strictValidator,
        stringResourceProvider
    )

    @Provides
    fun provideGetAppInfoUseCase(
        appInfoProvider: AppInfoProvider,
        stringResourceProvider: StringResourceProvider
    ): GetAppInfoUseCase = GetAppInfoUseCaseImpl(
        appInfoProvider,
        stringResourceProvider
    )

    @Provides
    fun provideReadBytesFromUriUseCase(
        uriStreamProvider: UriStreamProvider,
        stringResourceProvider: StringResourceProvider
    ): ReadBytesFromUriUseCase = ReadBytesFromUriUseCaseImpl(
        uriStreamProvider,
        stringResourceProvider
    )
}