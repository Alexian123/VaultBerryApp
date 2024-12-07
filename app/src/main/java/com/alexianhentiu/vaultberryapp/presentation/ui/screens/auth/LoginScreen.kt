package com.alexianhentiu.vaultberryapp.presentation.ui.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Login successful!")
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            key = "vaultKey",
                            value = vaultKey
                        )
                        navController.navigate("vault")
                    }

                ) {
                    Text("Enter vault")
                }
            }
        }
        is LoginState.Error -> {
            val errorMessage = (loginState as LoginState.Error).message
            Text("Error: $errorMessage", color = Color.Red)
        }
    }
}