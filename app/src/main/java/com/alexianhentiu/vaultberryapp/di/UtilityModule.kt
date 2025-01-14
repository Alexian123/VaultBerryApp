package com.alexianhentiu.vaultberryapp.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.domain.utils.VaultGuardian
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.AESHandler
import com.alexianhentiu.vaultberryapp.domain.utils.cryptography.CryptographyHandler
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
    fun provideCryptoHandler(): CryptographyHandler = AESHandler("CBC/PKCS5Padding")

    @Provides
    @Singleton
    fun provideVaultGuardian(cryptoHandler: CryptographyHandler): VaultGuardian = VaultGuardian(cryptoHandler)

    @Provides
    @Singleton
    fun provideInputValidator(): InputValidator = InputValidator()

    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun provideAccelerometer(sensorManager: SensorManager): Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
}