package com.alexianhentiu.vaultberryapp.presentation.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.service.autofill.Dataset
import android.service.autofill.Field
import android.service.autofill.FillResponse
import android.service.autofill.Presentations
import android.view.autofill.AutofillId
import android.view.autofill.AutofillManager
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.fragment.app.FragmentActivity
import com.alexianhentiu.vaultberryapp.presentation.ui.handlers.biometric.BiometricPromptHandler
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.autofill.AutofillScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme
import com.alexianhentiu.vaultberryapp.domain.model.AutofillEntry
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.AppTheme
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.sharedViewModels.UtilityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class AutofillActivity : FragmentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val utilityViewModel: UtilityViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels()
    private val biometricViewModel: BiometricViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val keywords = intent.getStringArrayListExtra("keywords") ?: return finish()
        val usernameId = intent.getParcelableExtra<AutofillId>(
            "usernameId",
            AutofillId::class.java
        ) ?: return finish()
        val passwordId = intent.getParcelableExtra<AutofillId>(
            "passwordId",
            AutofillId::class.java
        ) ?: return finish()

        setContent {
            val useSystemTheme = settingsViewModel.useSystemTheme.collectAsState()
            val darkTheme = settingsViewModel.darkTheme.collectAsState()

            // set current theme based on user preferences
            val currentTheme =
                if (useSystemTheme.value) remember { mutableStateOf(AppTheme.FOLLOW_SYSTEM) }
                else if (darkTheme.value) remember { mutableStateOf(AppTheme.DARK) }
                else remember { mutableStateOf(AppTheme.LIGHT) }

            VaultBerryAppTheme(appTheme = currentTheme.value) {
                AutofillScreen(
                    utilityViewModel = utilityViewModel,
                    sessionViewModel = sessionViewModel,
                    settingsViewModel = settingsViewModel,
                    biometricViewModel = biometricViewModel,
                    keywords = keywords,
                    onSuccess = { entries ->
                        val fillResponse = buildFillResponse(usernameId, passwordId, entries)
                        val resultIntent = Intent().apply {
                            putExtra(AutofillManager.EXTRA_AUTHENTICATION_RESULT, fillResponse)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                )
                BiometricPromptHandler(
                    fragmentActivity = this,
                    biometricViewModel = biometricViewModel
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun buildFillResponse(
        usernameId: AutofillId,
        passwordId: AutofillId,
        entries: List<AutofillEntry>
    ): FillResponse {
        val responseBuilder = FillResponse.Builder()
        entries.forEach { entry ->
            val usernameField = Field.Builder()
                .setValue(AutofillValue.forText(entry.username))
                .build()

            val passwordField = Field.Builder()
                .setValue(AutofillValue.forText(entry.password))
                .build()

            val presentation = RemoteViews(packageName, android.R.layout.simple_list_item_1).apply {
                setTextViewText(android.R.id.text1, entry.username)
            }
            val presentations = Presentations.Builder()
                .setDialogPresentation(presentation)
                .setMenuPresentation(presentation)
                .build()

            val dataset = Dataset.Builder(presentations)
                .setField(usernameId, usernameField)
                .setField(passwordId, passwordField)
                .build()

            responseBuilder.addDataset(dataset)
        }
        return responseBuilder.build()
    }
}
