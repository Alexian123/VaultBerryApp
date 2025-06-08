package com.alexianhentiu.vaultberryapp

import android.app.Application
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidNetworkClientInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var networkClientInitializer: AndroidNetworkClientInitializer

    private val applicationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            try {
                networkClientInitializer.initialize()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}