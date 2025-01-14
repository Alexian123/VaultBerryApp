package com.alexianhentiu.vaultberryapp.domain.usecase.sensor

import android.hardware.SensorEventListener
import android.hardware.SensorManager

class UnregisterListenerUseCase(private val sensorManager: SensorManager) {

    operator fun invoke(listener: SensorEventListener) {
        sensorManager.unregisterListener(listener)
    }
}