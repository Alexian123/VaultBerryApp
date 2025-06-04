package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.CheckboxOptionRow
import com.alexianhentiu.vaultberryapp.presentation.ui.common.enums.NavRoute

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    savedEmail: String = "",
    rememberEmail: Boolean = false,
    onBiometricLoginClicked: () -> Unit = {},
    isBiometricAuthAvailable: Boolean = false,
    onLoginClicked: (String, String, Boolean) -> Unit,
    onForgotPasswordClicked: () -> Unit,
    validator: (InputType) -> (String) -> Boolean = { { true } }
) {
    var email by remember(rememberEmail, savedEmail) {
        mutableStateOf(if (rememberEmail) savedEmail else "")
    }
    var password by remember { mutableStateOf("") }

    var isEmailValid by remember(email) {
        mutableStateOf(
            validator(InputType.EMAIL)(email)
        )
    }
    var isPasswordValid by remember {
        mutableStateOf(
            validator(InputType.PASSWORD)(password)
        )
    }

    var rememberEmailChecked by remember(rememberEmail) { mutableStateOf(rememberEmail) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ValidatedTextField(
            label = stringResource(R.string.email_label),
            initialText = email,
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
            },
            isValid = isPasswordValid,
            validate = validator(InputType.PASSWORD),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            CheckboxOptionRow(
                text = stringResource(R.string.remember_email_label),
                checked = rememberEmailChecked,
                onCheckedChange = { rememberEmailChecked = it }
            )
            IconButton(
                onClick = onBiometricLoginClicked,
                enabled = isBiometricAuthAvailable
            ) {
                Icon(Icons.Filled.Fingerprint, stringResource(R.string.biometric_icon_description))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLoginClicked(email, password, rememberEmailChecked) },
            enabled = isEmailValid && isPasswordValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login_button_text))
        }
        TextButton(
            onClick = { navController.navigate(NavRoute.REGISTER.path) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.register_button_text))
        }
        TextButton(
            onClick = { onForgotPasswordClicked() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.forgot_password_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    LoginForm(
        navController = NavHostController(LocalContext.current),
        onLoginClicked = { _, _, _ -> },
        onForgotPasswordClicked = {}
    )
}