package com.alexianhentiu.vaultberryapp.domain.usecase.sensor

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class RegisterListenerUseCase(private val sensorManager: SensorManager) {

    operator fun invoke(listener: SensorEventListener, sensor: Sensor) {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)
    }
}