package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.common.enums.InputType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun RecoveryLoginForm(
    onSendClicked: (String) -> Unit,
    onLoginClicked: (String, String) -> Unit,
    otpRequested: Boolean,
    validator: (InputType) -> (String) -> Boolean = { { true } }
) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(false) }

    var recoveryPassword by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    var isRecoveryPasswordValid by remember { mutableStateOf(false) }
    var isOTPValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.otp_request_text),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Left
        )
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
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onSendClicked(email) },
            enabled = isEmailValid,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.send_button_text))
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 64.dp))
        PasswordField(
            enabled = otpRequested,
            onPasswordChange = { newPassword, valid ->
                recoveryPassword = newPassword
                isRecoveryPasswordValid = valid
            },
            label = stringResource(R.string.recovery_password_label),
            validate = validator(InputType.PASSWORD),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            enabled = otpRequested,
            onPasswordChange = { newPassword, valid ->
                otp = newPassword
                isOTPValid = valid
            },
            label = stringResource(R.string.otp_label),
            validate = validator(InputType.OTP),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onLoginClicked(recoveryPassword, otp) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isRecoveryPasswordValid && isOTPValid && otpRequested
        ) {
            Text(stringResource(R.string.login_button_text))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoveryLoginFormPreview() {
    RecoveryLoginForm(
        onSendClicked = {},
        onLoginClicked = { _, _ -> },
        otpRequested = false
    )
}