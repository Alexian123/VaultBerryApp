package com.alexianhentiu.vaultberryapp

import android.app.Application
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidFileLoggingTree
import com.alexianhentiu.vaultberryapp.data.platform.utils.AndroidNetworkClientInitializer
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var networkClientInitializer: AndroidNetworkClientInitializer

    @Inject
    lateinit var fileLoggingTree: AndroidFileLoggingTree

    private val applicationScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        Timber.plant(fileLoggingTree)
        Timber.plant(Timber.DebugTree())
        applicationScope.launch {
            try {
                networkClientInitializer.initialize()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}