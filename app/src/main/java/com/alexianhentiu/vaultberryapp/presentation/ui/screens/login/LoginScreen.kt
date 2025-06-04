package com.alexianhentiu.vaultberryapp.presentation.ui.screens.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.AppInfo
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.SuccessAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.ui.common.EmailIntentUtils.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.ui.common.SessionState
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.InfoDialog
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.BiometricViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    sessionViewModel: SessionViewModel,
    utilityViewModel: UtilityViewModel,
    settingsViewModel: SettingsViewModel,
    biometricViewModel: BiometricViewModel,
) {
    val screenState by sessionViewModel.sessionState.collectAsState()

    val savedEmail by settingsViewModel.savedEmail.collectAsState()
    val rememberEmail by settingsViewModel.rememberEmail.collectAsState()
    val biometricEnabled by settingsViewModel.biometricEnabled.collectAsState()

    LaunchedEffect(Unit) {
        biometricViewModel.credentialsEvent.collect { credentials ->
            sessionViewModel.login(credentials.email, credentials.password)
        }
    }

    BackHandler(enabled = true) {}

    Scaffold(
        topBar = {
            AuthTopBar(
                onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) },
                titleText = stringResource(R.string.login_screen_title)
            )
        }
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            when (screenState) {
                is SessionState.LoggedOut -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LoginForm(
                            navController = navController,
                            savedEmail = savedEmail,
                            rememberEmail = rememberEmail,
                            onBiometricLoginClicked = {
                                biometricViewModel.requestAuthenticateAndRetrieveCredentials()
                            },
                            isBiometricAuthAvailable = biometricEnabled,
                            onLoginClicked = { email, password, rememberEmailChecked ->
                                if (rememberEmailChecked) {
                                    settingsViewModel.setSavedEmail(email)
                                    settingsViewModel.setRememberEmail(true)
                                } else {
                                    settingsViewModel.setRememberEmail(false)
                                }
                                sessionViewModel.login(email, password)
                            },
                            onForgotPasswordClicked = {
                                navController.navigate(NavRoute.RECOVERY.path)
                            },
                            validator = {
                                utilityViewModel.getValidatorFunction(it, true)
                            },
                            modifier = Modifier.weight(1f)
                        )
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
                        onCancelClicked = { sessionViewModel.resetState() },
                        validator = {
                            utilityViewModel.getValidatorFunction(it, true)
                        }
                    )
                }

                is SessionState.LoggedIn -> {
                    val credentials by sessionViewModel
                        .credentialsEvent.collectAsState(initial = null)
                    val hasStoredCredentials by biometricViewModel
                        .hasStoredCredentials.collectAsState()
                    LaunchedEffect(credentials) {
                        if (credentials != null && biometricEnabled && !hasStoredCredentials) {
                            biometricViewModel.requestStoreCredentials(
                                credentials!!.email,
                                credentials!!.password
                            )
                        }
                    }
                    var shownAnimation by remember { mutableStateOf(false) }
                    if (!shownAnimation) {
                        SuccessAnimationDialog(
                            displayDurationMillis = 1000,
                            onTimeout = {
                                shownAnimation = true
                                navController.navigate(NavRoute.VAULT.path)
                            }
                        )
                    }
                }

                is SessionState.Error -> {
                    val context = LocalContext.current
                    val errorInfo = (screenState as SessionState.Error).info
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