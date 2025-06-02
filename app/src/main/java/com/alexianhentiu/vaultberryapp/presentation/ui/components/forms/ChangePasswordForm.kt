package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.domain.utils.enums.ValidatedFieldType
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField

@Composable
fun ChangePasswordForm(
    onChangePassword: (String, Boolean) -> Unit,
    validator: (ValidatedFieldType) -> (String) -> Boolean = { { true } },
    evaluatePasswordStrength: (String) -> PasswordStrength = { PasswordStrength.NONE },
    textFieldType: TextFieldType = TextFieldType.OUTLINED
) {
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var isConfirmPasswordValid by remember { mutableStateOf(false) }
    var shouldReEncrypt by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(8.dp)) {
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                password = newPassword
                isPasswordValid = valid
                isConfirmPasswordValid = password == confirmPassword
            },
            isValid = isPasswordValid,
            validate = validator(ValidatedFieldType.PASSWORD),
            label = stringResource(R.string.new_password_label),
            evaluateStrength = evaluatePasswordStrength,
            showStrengthIndicator = true,
            textFieldType = textFieldType
        )
        PasswordField(
            onPasswordChange = { newPassword, valid ->
                confirmPassword = newPassword
                isConfirmPasswordValid = valid
            },
            isValid = isConfirmPasswordValid,
            validate = password::equals,
            label = stringResource(R.string.confirm_new_password_label),
            textFieldType = textFieldType
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = shouldReEncrypt,
                onCheckedChange = { shouldReEncrypt = it }
            )
            Text(stringResource(R.string.re_encrypt_label))
        }
        Button(
            enabled = isPasswordValid && isConfirmPasswordValid && password == confirmPassword,
            onClick = { onChangePassword(password, shouldReEncrypt) },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp)
        ) {
            Text(stringResource(R.string.change_password_button_text))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChangePasswordFormPreview() {
    ChangePasswordForm(
        onChangePassword = { _, _ -> },
    )
}