package com.alexianhentiu.vaultberryapp.presentation.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.topBars.AuthTopBar
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.AnimatedSuccessScreen
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.utils.state.SessionState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SessionViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.SettingsViewModel

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val activity = LocalActivity.current as ComponentActivity
    val sessionViewModel: SessionViewModel = hiltViewModel(activity)
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)
    val settingsViewModel: SettingsViewModel = hiltViewModel(activity)

    val savedEmail by settingsViewModel.savedEmail.collectAsState()
    val rememberEmail by settingsViewModel.rememberEmail.collectAsState()

    val inputValidator by utilityViewModel.inputValidator.collectAsState()

    val screenState by sessionViewModel.sessionState.collectAsState()

    BackHandler(enabled = true) {}

    when (screenState) {
        is SessionState.LoggedOut -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
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
            }
        }

        is SessionState.Loading -> {
            LoadingScreen()
        }

        is SessionState.TwoFactorRequired -> {
            Scaffold(
                topBar = {
                    AuthTopBar(
                        onSettingsClick = { navController.navigate(NavRoute.SETTINGS.path) }
                    )
                }
            ) { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
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
            }
        }

        is SessionState.LoggedIn -> {
            AnimatedSuccessScreen(
                displayDurationMillis = 1000,
                onTimeout = { navController.navigate(NavRoute.VAULT.path) }
            )
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