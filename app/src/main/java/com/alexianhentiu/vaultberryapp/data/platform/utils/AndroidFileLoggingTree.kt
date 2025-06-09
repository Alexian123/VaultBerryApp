package com.alexianhentiu.vaultberryapp.data.platform.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidFileLoggingTree @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : Timber.DebugTree() {

    private val logFileName = "logs.txt"
    private val logDirName = "Logs"
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return // Don't log verbose or debug to file in production
        }

        val logMessage = StringBuilder()
        logMessage.append(dateFormatter.format(Date()))
        logMessage.append(" [${getPriorityString(priority)}]")
        tag?.let { logMessage.append(" [$it]") }
        logMessage.append(": ").append(message)
        t?.let { logMessage.append("\n").append(Log.getStackTraceString(it)) }
        logMessage.append("\n")

        try {
            val logDir = File(applicationContext.externalCacheDir, logDirName)
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            val logFile = File(logDir, logFileName)
            FileWriter(logFile, true).use { writer ->
                writer.append(logMessage.toString())
            }
        } catch (e: IOException) {
            Timber.tag("AndroidFileLoggingTree").e(e, "Error writing log to file: ${e.message}")
        }
    }

    private fun getPriorityString(priority: Int): String {
        return when (priority) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            Log.ASSERT -> "A"
            else -> "?"
        }
    }
}