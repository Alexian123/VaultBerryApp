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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute

@Composable
fun RegisterForm(
    navController: NavHostController,
    onRegisterClicked: (String, String, String?, String?) -> Unit,
    validator: (InputType) -> (String) -> Boolean = { { true } },
    evaluatePasswordStrength: (String) -> PasswordStrength = { PasswordStrength.NONE }
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    var isEmailValid by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(false) }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(R.string.first_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(R.string.last_name_label)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ValidatedTextField(
            label = stringResource(R.string.email_label),
            onInputChange = { newEmail, valid ->
                email = newEmail
                isEmailValid = valid
            },
            isValid = isEmailValid,
            validate = validator(InputType.EMAIL),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                password = newPassword
                isPasswordValid = valid
                isConfirmPasswordValid = password == confirmPassword
            },
            isValid = isPasswordValid,
            validate = validator(InputType.PASSWORD),
            showStrengthIndicator = true,
            evaluateStrength = evaluatePasswordStrength,
            modifier = Modifier.fillMaxWidth()
        )
        if (!isPasswordValid) {
            Text(
                text = stringResource(R.string.password_validity_hint),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(4.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                confirmPassword = newPassword
                isConfirmPasswordValid = valid
            },
            isValid = isConfirmPasswordValid,
            validate = password::equals,
            label = stringResource(R.string.confirm_password_label),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRegisterClicked(email, password, firstName, lastName) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isEmailValid && isPasswordValid && isConfirmPasswordValid
                    && password == confirmPassword
        ) {
            Text(stringResource(R.string.register_button_text))
        }
        TextButton(
            onClick = { navController.navigate(NavRoute.LOGIN.path) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.already_have_account_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    RegisterForm(
        navController = NavHostController(LocalContext.current),
        onRegisterClicked = { _, _, _, _ -> }
    )
}