package com.alexianhentiu.vaultberryapp.presentation.ui.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.net.toUri
import com.alexianhentiu.vaultberryapp.domain.model.ErrorInfo

object EmailIntentUtils {

    fun createEmailIntent(
        recipientEmail: String,
        subject: String = "",
        body: String = ""
    ): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            if (subject.isNotBlank()) {
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            if (body.isNotBlank()) {
                putExtra(Intent.EXTRA_TEXT, body)
            }
        }
    }


    /**
     * Creates an Intent to send an error report via email.
     *
     * @param context Context used to get app and device information.
     * @param errorInfo The ErrorInfo object containing details about the error.
     * @param recipientEmail The email address to send the report to.
     * @return An Intent configured to send an email with the error report details.
     */
    fun launchErrorReportEmailIntent(
        context: Context,
        errorInfo: ErrorInfo,
        recipientEmail: String,
        appName: String,
        appVersionName: String,
    ) {
        val appVersion = appVersionName
        val deviceManufacturer = Build.MANUFACTURER
        val deviceModel = Build.MODEL
        val androidVersion = Build.VERSION.RELEASE
        val androidApiLevel = Build.VERSION.SDK_INT

        val body = """
        IMPORTANT: Please describe what you were doing when the error occurred.
        Any additional details can help us fix the issue faster!
        ----------------------------------------------------

        Error Report Details:
        ----------------------------------------------------
        App Version: $appVersion
        Error Type: ${errorInfo.type.name}
        Error Source: ${errorInfo.source}
        Error Message: ${errorInfo.message}
        ----------------------------------------------------
        Device Information:
        ----------------------------------------------------
        Manufacturer: $deviceManufacturer
        Model: $deviceModel
        Android Version: $androidVersion (API $androidApiLevel)
        ----------------------------------------------------
        User Description (Please fill below):
        





        ----------------------------------------------------
    """.trimIndent()

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, "$appName Error Report")
            putExtra(Intent.EXTRA_TEXT, body)
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.e("ErrorReport", "No email client found to send the report.", e)
        } catch (e: Exception) {
            Log.e("ErrorReport", "Error sending the report via email.", e)
        }
    }

}