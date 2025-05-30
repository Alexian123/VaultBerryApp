package com.alexianhentiu.vaultberryapp.presentation.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.SuccessAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.helper.launchErrorReportEmailIntent
import com.alexianhentiu.vaultberryapp.presentation.utils.state.BiometricState
import com.alexianhentiu.vaultberryapp.presentation.utils.state.SessionState
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
) {
    val savedEmail by settingsViewModel.savedEmail.collectAsState()
    val rememberEmail by settingsViewModel.rememberEmail.collectAsState()

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val screenState by sessionViewModel.sessionState.collectAsState()

    val activity = (LocalContext.current) as FragmentActivity
    val biometricViewModel: BiometricViewModel = hiltViewModel()
    val biometricState by biometricViewModel.biometricState.collectAsState()
    val hasStoredCredentials by biometricViewModel.hasStoredCredentials.collectAsState()
    biometricViewModel.checkStoredCredentials()

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
                    LoginForm(
                        navController = navController,
                        savedEmail = savedEmail,
                        rememberEmail = rememberEmail,
                        onLoginClicked = { email, password, rememberEmailChecked ->
                            if (rememberEmailChecked) {
                                settingsViewModel.setSavedEmail(email)
                                settingsViewModel.setRememberEmail(true)
                            } else {
                                settingsViewModel.setRememberEmail(false)
                            }
                            if (!hasStoredCredentials) {
                                biometricViewModel.storeCredentials(
                                    activity,
                                    email,
                                    password
                                )
                                when (biometricState) {
                                    is BiometricState.CredentialsStored -> {
                                        Log.d("LoginScreen", "Credentials stored")
                                        sessionViewModel.login(email, password)
                                    }
                                    is BiometricState.Error -> {
                                        val errorInfo = (biometricState as BiometricState.Error).info
                                        Log.d("LoginScreen", "Error storing credentials: ${errorInfo.message}")
                                    }
                                    else -> {
                                        Log.d("LoginScreen", "Biometric state: $biometricState")
                                    }
                                }
                            } else {
                                biometricViewModel.authenticate(activity)
                                when (biometricState) {
                                    is BiometricState.Authenticated -> {
                                        Log.d("LoginScreen", "Credentials authenticated")
                                        Log.d("LoginScreen", "Email: $email, Password: $password")
                                    }
                                    else -> {
                                        Log.d("LoginScreen", "Biometric state: $biometricState")
                                    }
                                }
                            }
                            //sessionViewModel.login(email, password)
                        },
                        onForgotPasswordClicked = { navController.navigate(NavRoute.RECOVERY.path) },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }

                is SessionState.Loading -> {
                    LoadingAnimationDialog()
                }

                is SessionState.TwoFactorRequired -> {
                    Verify2FAForm(
                        onContinueClicked = { code ->
                            sessionViewModel.login(null, null, code)
                        },
                        onCancelClicked = { sessionViewModel.resetState() },
                        validator = {
                            inputValidator?.getValidatorFunction(it) ?: { false }
                        }
                    )
                }

                is SessionState.LoggedIn -> {
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
                    val errorInfo = (screenState as SessionState.Error).info
                    val context = LocalContext.current
                    val contactEmail = stringResource(R.string.contact_email)
                    ErrorDialog(
                        onConfirm = { sessionViewModel.resetState() },
                        title = "${errorInfo.type.name} ERROR",
                        source = errorInfo.source,
                        message = errorInfo.message,
                        onSendReport = {
                            launchErrorReportEmailIntent(
                                context = context,
                                errorInfo = errorInfo,
                                recipientEmail = contactEmail
                            )
                        }
                    )
                }
            }
        }
    }
}