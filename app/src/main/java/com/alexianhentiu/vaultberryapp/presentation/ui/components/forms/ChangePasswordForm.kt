package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.validation.DebugValidator
import com.alexianhentiu.vaultberryapp.domain.utils.validation.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField

@Composable
fun ChangePasswordForm(
    onChangePassword: (String) -> Unit,
    inputValidator: InputValidator,
    textFieldType: TextFieldType = TextFieldType.OUTLINED
) {
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                password = newPassword
                isPasswordValid = valid
            },
            isValid = inputValidator::validatePassword,
            label = "New Password",
            textFieldType = textFieldType
        )
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                confirmPassword = newPassword
                isConfirmPasswordValid = valid
            },
            isValid = password::equals,
            label = "Confirm New Password",
            textFieldType = textFieldType
        )
        Button(
            enabled = isPasswordValid && isConfirmPasswordValid && password == confirmPassword,
            onClick = { onChangePassword(password) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        ) {
            Text("Change Password")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChangePasswordFormPreview() {
    ChangePasswordForm(
        onChangePassword = {},
        inputValidator = DebugValidator()
    )
}