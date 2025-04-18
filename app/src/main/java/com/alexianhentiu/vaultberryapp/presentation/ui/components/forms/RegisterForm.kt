package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.utils.NavigationManager
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.NavRoute

@Composable
fun RegisterForm(
    navManager: NavigationManager,
    onRegisterClicked: (String, String, String?, String?) -> Unit,
    inputValidator: InputValidator
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        ValidatedTextField(
            label = "Email",
            onInputChange = { newEmail, valid ->
                email = newEmail
                isEmailValid = valid
            },
            isValid = inputValidator::validateEmail,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                password = newPassword
                isPasswordValid = valid
            },
            isValid = inputValidator::validatePassword,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRegisterClicked(email, password, firstName, lastName) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEmailValid && isPasswordValid
        ) {
            Text("Register")
        }
        TextButton(
            onClick = { navManager.navigate(NavRoute.LOGIN) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account?")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    RegisterForm(
        navManager = NavigationManager(NavController(LocalContext.current)),
        onRegisterClicked = { _, _, _, _ -> },
        inputValidator = InputValidator()
    )
}