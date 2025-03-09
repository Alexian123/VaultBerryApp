package com.alexianhentiu.vaultberryapp.presentation.ui.screens.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.ErrorDialog
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.LoginForm
import com.alexianhentiu.vaultberryapp.presentation.ui.components.forms.Verify2FAForm
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.extra.LoadingScreen
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.state.LoginState

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController
) {
    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.LoggedOut -> {
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    LoginForm(
                        navController = navController,
                        onLoginClicked = { email, password -> viewModel.login(email, password) },
                        onForgotPasswordClicked = { navController.navigate("recovery") },
                        inputValidator = viewModel.inputValidator
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
            Scaffold { contentPadding ->
                Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
                    Verify2FAForm(
                        onContinueClicked = { code ->
                            viewModel.verify2FA(email, password, code)
                        },
                        onCancelClicked = { viewModel.resetState() },
                        inputValidator = viewModel.inputValidator
                    )
                }
            }
        }

        is LoginState.LoggedIn -> {
            val currentLoginState = loginState as LoginState.LoggedIn
            val vaultKey = currentLoginState.decryptedKey
            // send key to vault screen
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "vaultKey",
                value = vaultKey
            )
            navController.navigate("vault")
        }

        is LoginState.Error -> {
            val errorMessage = (loginState as LoginState.Error).message
            ErrorDialog(
                onConfirm = { viewModel.resetState() },
                title = "Login Error",
                message = errorMessage
            )
        }
    }
}