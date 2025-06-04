package com.alexianhentiu.vaultberryapp.presentation.ui.screens.autofill

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.SuccessAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.domain.model.AutofillEntry
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.ui.common.SessionState
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.SettingsViewModel
import com.alexianhentiu.vaultberryapp.presentation.ui.common.viewmodels.UtilityViewModel

@Composable
fun AutofillScreen(
    utilityViewModel: UtilityViewModel,
    sessionViewModel: SessionViewModel,
    settingsViewModel: SettingsViewModel,
    biometricViewModel: BiometricViewModel,
    autofillViewModel: AutofillViewModel = hiltViewModel(),
    keywords: List<String>,
    onSuccess: (List<AutofillEntry>) -> Unit,
) {
    val savedEmail by settingsViewModel.savedEmail.collectAsState()
    val rememberEmail by settingsViewModel.rememberEmail.collectAsState()
    val biometricEnabled by settingsViewModel.biometricEnabled.collectAsState()

    val screenState by sessionViewModel.sessionState.collectAsState()

    val hasStoredCredentials by biometricViewModel.hasStoredCredentials.collectAsState()

    var finishedSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        biometricViewModel.credentialsEvent.collect { credentials ->
            sessionViewModel.login(credentials.email, credentials.password)
        }
    }

    BackHandler(enabled = true) {}

    Scaffold { contentPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
            when (screenState) {
                is SessionState.LoggedOut -> {
                    var email by remember(savedEmail, rememberEmail) {
                        mutableStateOf(if (rememberEmail) savedEmail else "")
                    }
                    var password by remember { mutableStateOf("") }

                    var isEmailValid by remember(email) {
                        mutableStateOf(
                            utilityViewModel.getValidatorFunction(
                                InputType.EMAIL,
                                true
                            ).invoke(email) == true
                        )
                    }

                    var isPasswordValid by remember { mutableStateOf(false) }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        ValidatedTextField(
                            label = stringResource(R.string.email_label),
                            initialText = email,
                            onInputChange = { newEmail, valid ->
                                email = newEmail
                                isEmailValid = valid
                            },
                            isValid = isEmailValid,
                            validate = utilityViewModel.getValidatorFunction(
                                InputType.EMAIL,
                                true
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordField(
                            onPasswordChange = { newPassword, valid ->
                                password = newPassword
                                isPasswordValid = valid
                            },
                            isValid = isPasswordValid,
                            validate = utilityViewModel.getValidatorFunction(
                                InputType.PASSWORD,
                                true
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                sessionViewModel.login(email, password)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = isEmailValid && isPasswordValid && !finishedSearching
                        ) {
                            Text(stringResource(R.string.login_button_text))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        IconButton(
                            onClick = {
                                if (hasStoredCredentials) {
                                    biometricViewModel.requestAuthenticateAndRetrieveCredentials()
                                }
                            },
                            enabled = biometricEnabled,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Filled.Fingerprint,
                                stringResource(R.string.biometric_icon_description)
                            )
                        }
                    }
                }

                is SessionState.Loading -> {
                    LoadingAnimationDialog()
                }

                is SessionState.ActivationEmailSent -> {
                    InfoDialog(
                        title = stringResource(R.string.activation_required_title),
                        message = stringResource(R.string.activation_required_message),
                        onDismissRequest = {
                            sessionViewModel.resetState()
                        }
                    )
                }

                is SessionState.TwoFactorRequired -> {
                    Verify2FAForm(
                        onContinueClicked = { code ->
                            sessionViewModel.login(null, null, code)
                        },
                        onCancelClicked = {
                            autofillViewModel.resetState()
                            sessionViewModel.resetState()
                        },
                        validator = {
                            utilityViewModel.getValidatorFunction(it, true)
                        }
                    )
                }

                is SessionState.LoggedIn -> {
                    val autofillState by autofillViewModel.state.collectAsState()
                    when (autofillState) {
                        is AutofillState.Idle -> {
                            val decryptedKey by sessionViewModel.decryptedKey.collectAsState()
                            autofillViewModel.searchVaultEntries(keywords, decryptedKey)
                        }

                        is AutofillState.Loading -> {
                            LoadingAnimationDialog()
                        }

                        is AutofillState.Success -> {
                            val suggestions by autofillViewModel.autofillSuggestions.collectAsState()
                            var showAnimation by remember { mutableStateOf(false) }
                            if (!showAnimation) {
                                SuccessAnimationDialog(
                                    displayDurationMillis = 1000,
                                    onTimeout = {
                                        showAnimation = true
                                        finishedSearching = true
                                        onSuccess(suggestions)
                                        autofillViewModel.clearSuggestions()
                                        sessionViewModel.logout()
                                    }
                                )
                            }
                        }

                        is AutofillState.Error -> {
                            val errorInfo = (autofillState as AutofillState.Error).info
                            val context = LocalContext.current
                            val contactEmail = stringResource(R.string.contact_email)
                            ErrorDialog(
                                onConfirm = { autofillViewModel.resetState() },
                                errorInfo = errorInfo,
                                onSendReport = {
                                    launchErrorReportEmailIntent(
                                        context = context,
                                        errorInfo = errorInfo,
                                        recipientEmail = contactEmail,
                                        appName = utilityViewModel.getAppInfo(AppInfo.APP_NAME),
                                        appVersionName = utilityViewModel.getAppInfo(
                                            AppInfo.VERSION_NAME
                                        ),
                                    )
                                }
                            )
                        }
                    }
                }

                is SessionState.Error -> {
                    val errorInfo = (screenState as SessionState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { sessionViewModel.resetState() },
                        errorInfo = errorInfo,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail,
                                appName = utilityViewModel.getAppInfo(AppInfo.APP_NAME),
                                appVersionName = utilityViewModel.getAppInfo(
                                    AppInfo.VERSION_NAME
                                ),
                            )
                        }
                    )
                }
            }
        }
    }
}