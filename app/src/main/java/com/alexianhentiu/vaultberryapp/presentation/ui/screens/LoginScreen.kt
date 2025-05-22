package com.alexianhentiu.vaultberryapp.presentation.ui.screens

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
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.SuccessAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated.LoadingAnimationDialog
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.state.SessionState
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

    BackHandler(enabled = true) {}

    Scaffold(
        topBar = {
            AuthTopBar(
                onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) },
                titleText = "Login to VaultBerry"
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
                            sessionViewModel.login(email, password)
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
                    val errorMessage = (screenState as SessionState.Error).info.message
                    ErrorDialog(
                        onConfirm = { sessionViewModel.resetState() },
                        title = "Login Error",
                        message = errorMessage
                    )
                }
            }
        }
    }
}