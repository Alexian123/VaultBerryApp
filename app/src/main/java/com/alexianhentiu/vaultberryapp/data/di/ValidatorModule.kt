package com.alexianhentiu.vaultberryapp.data.di

import com.alexianhentiu.vaultberryapp.data.di.qualifiers.LaxInputValidatorQualifier
import com.alexianhentiu.vaultberryapp.data.di.qualifiers.StrictInputValidatorQualifier
import com.alexianhentiu.vaultberryapp.data.validation.LaxInputValidator
import com.alexianhentiu.vaultberryapp.data.validation.StrictInputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ValidatorModule {
    @Provides
    @Singleton
    @LaxInputValidatorQualifier
    fun provideLaxInputValidator(): InputValidator {
        return LaxInputValidator()
    }

    @Provides
    @Singleton
    @StrictInputValidatorQualifier
    fun provideStrictInputValidator(): InputValidator {
        return StrictInputValidator()
    }
}