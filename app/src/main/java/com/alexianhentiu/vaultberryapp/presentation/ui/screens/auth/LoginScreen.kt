package com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.presentation.ui.state.LoginState
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController) {
    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Idle -> {
            LoginForm(
                navController = navController,
                onLoginClicked = { email, password -> viewModel.login(email, password) }
            )
        }
        is LoginState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is LoginState.Success -> {
            val vaultKey = (loginState as LoginState.Success).decryptedVaultKey
            Text("Login successful!")
        }
        is LoginState.Error -> {
            val errorMessage = (loginState as LoginState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}