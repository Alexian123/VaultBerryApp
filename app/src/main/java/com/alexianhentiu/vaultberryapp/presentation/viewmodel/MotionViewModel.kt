package com.alexianhentiu.vaultberryapp.presentation.viewmodel

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexianhentiu.vaultberryapp.domain.usecase.core.sensor.RegisterListenerUseCase
import com.alexianhentiu.vaultberryapp.domain.usecase.core.sensor.UnregisterListenerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MotionViewModel @Inject constructor(
    private val accelerometer: Sensor?,
    private val registerListenerUseCase: RegisterListenerUseCase,
    private val unregisterListenerUseCase: UnregisterListenerUseCase
): ViewModel() {

    private val _motionDetected = MutableStateFlow(false)
    val motionDetected = _motionDetected.asStateFlow()

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            viewModelScope.launch {
                val x = p0?.values?.get(0) ?: 0f
                val y = p0?.values?.get(1) ?: 0f
                val z = p0?.values?.get(2) ?: 0f
                val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                if (magnitude > 15.0f) {
                    Log.d("MotionViewModel", "Magnitude above threshold: $magnitude")
                    _motionDetected.emit(true)
                    unregisterSensorListener()
                }
            }
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit
    }

    fun registerSensorListener() {
        Log.d("MotionViewModel", "Registering sensor listener")
        if (accelerometer == null) {
            Log.e("MotionViewModel", "Missing accelerometer sensor")
        } else {
            registerListenerUseCase(sensorEventListener, accelerometer)
        }
    }

    fun unregisterSensorListener() {
        Log.d("MotionViewModel", "Unregistering sensor listener")
        unregisterListenerUseCase(sensorEventListener)
    }

    fun resetMotionDetected() {
        viewModelScope.launch {
            _motionDetected.emit(false)
        }
    }
}