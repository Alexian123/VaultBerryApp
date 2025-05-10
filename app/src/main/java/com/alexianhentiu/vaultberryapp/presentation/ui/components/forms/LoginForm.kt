package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.domain.utils.types.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

@Composable
fun LoginForm(
    navManager: NavigationManager,
    onLoginClicked: (String, String) -> Unit,
    onForgotPasswordClicked: () -> Unit,
    validator: (ValidatedFieldType) -> (String) -> Boolean = { { true } }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ValidatedTextField(
            label = "Email",
            onInputChange = { newEmail, valid ->
                email = newEmail
                isEmailValid = valid
            },
            isValid = validator(ValidatedFieldType.EMAIL),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                password = newPassword
                isPasswordValid = valid
            },
            isValid = validator(ValidatedFieldType.PASSWORD),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLoginClicked(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text("Login")
        }
        TextButton(
            onClick = { navManager.navigate(NavRoute.REGISTER) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Register")
        }
        TextButton(
            onClick = { onForgotPasswordClicked() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Forgot password")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    LoginForm(
        navManager = NavigationManager(NavController(LocalContext.current)),
        onLoginClicked = { _, _ -> },
        onForgotPasswordClicked = {}
    )
}