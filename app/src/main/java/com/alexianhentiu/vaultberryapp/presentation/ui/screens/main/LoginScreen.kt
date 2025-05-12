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
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.unique.LoginViewModel
import com.alexianhentiu.vaultberryapp.presentation.utils.state.LoginState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.UtilityViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.shared.VaultKeyViewModel

@Composable
fun LoginScreen(
    navController: NavHostController
) {
    val activity = LocalActivity.current as ComponentActivity
    val vaultKeyViewModel: VaultKeyViewModel = hiltViewModel(activity)
    val utilityViewModel: UtilityViewModel = hiltViewModel(activity)

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()

    BackHandler(enabled = true) {}

    when (loginState) {
        is LoginState.LoggedOut -> {
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
                        onLoginClicked = { email, password -> loginViewModel.login(email, password) },
                        onForgotPasswordClicked = { navController.navigate(NavRoute.RECOVERY.path) },
                        validator = utilityViewModel::getFieldValidator
                    )
                }
            }
        }

        is LoginState.Loading -> {
            LoadingScreen()
        }

        is LoginState.Verify2FA -> {
            val currentLoginState = loginState as LoginState.Verify2FA
            val email = currentLoginState.email
            val password = currentLoginState.password
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
                            loginViewModel.verify2FA(email, password, code)
                        },
                        onCancelClicked = { loginViewModel.resetState() },
                        validator = utilityViewModel::getFieldValidator
                    )
                }
            }
        }

        is LoginState.LoggedIn -> {
            val currentLoginState = loginState as LoginState.LoggedIn
            vaultKeyViewModel.setDecryptedKey(currentLoginState.decryptedKey)
            navController.navigate(NavRoute.VAULT.path)
        }

        is LoginState.Error -> {
            val errorMessage = (loginState as LoginState.Error).info.message
            ErrorDialog(
                onConfirm = { loginViewModel.resetState() },
                title = "Login Error",
                message = errorMessage
            )
        }
    }
}