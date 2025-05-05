package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.security.PasswordEvaluator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.DebugValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField

@Composable
fun RecoveryLoginForm(
    onContinueClicked: (String, String) -> Unit,
    onCancelClicked: () -> Unit,
    inputValidator: InputValidator,
    passwordEvaluator: PasswordEvaluator
) {
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
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                recoveryPassword = newPassword
                isRecoveryPasswordValid = valid
            },
            label = "Recovery Password",
            isValid = inputValidator::validatePassword,
            passwordEvaluator = passwordEvaluator,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                otp = newPassword
                isOTPValid = valid
            },
            label = "One Time Password",
            isValid = inputValidator::validateOTP,
            passwordEvaluator = passwordEvaluator,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            OutlinedButton(
                onClick = { onCancelClicked() },
                modifier = Modifier.weight(0.4f)
            ) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                onClick = { onContinueClicked(recoveryPassword, otp) },
                modifier = Modifier.weight(0.4f),
                enabled = isRecoveryPasswordValid && isOTPValid
            ) {
                Text("Continue")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecoveryLoginFormPreview() {
    RecoveryLoginForm(
        onContinueClicked = { _, _ -> },
        onCancelClicked = {},
        inputValidator = DebugValidator(),
        passwordEvaluator = PasswordEvaluator()
    )
}