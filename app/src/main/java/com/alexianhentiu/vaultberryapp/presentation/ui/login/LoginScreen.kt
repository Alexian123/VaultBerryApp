package com.alexianhentiu.vaultberryapp.presentation.ui.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.alexianhentiu.vaultberryapp.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // Observe loginState in your Composable
    val loginState by viewModel.loginState.collectAsState()

    when (loginState) {
        is LoginState.Idle -> {
            // Display Idle state, usually an initial screen with login fields
            LoginForm(onLoginClicked = { email, password ->
                viewModel.login(email, password)
            })
        }
        is LoginState.Loading -> {
            // Show loading spinner when the login is in progress
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }
        is LoginState.Success -> {
            // Handle successful login
            val loginResponse = (loginState as LoginState.Success).loginResponse
            Text("Login successful!")
        }
        is LoginState.Error -> {
            // Show error message if the login failed
            val errorMessage = (loginState as LoginState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}