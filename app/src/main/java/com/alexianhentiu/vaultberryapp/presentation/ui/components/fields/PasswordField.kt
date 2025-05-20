package com.alexianhentiu.vaultberryapp.presentation.ui.components.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons.CopyToClipboardButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons.ToggleVisibilityButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.PasswordStrengthIndicator
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    onPasswordChange: (String, Boolean) -> Unit = { _, _ -> },
    initiallyVisible: Boolean = false,
    showToggleVisibilityButton: Boolean = true,
    initialText: String = "",
    isValid: (String) -> Boolean = { true },
    showCopyToClipboardButton: Boolean = false,
    onCopyClicked: (String) -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: String = "Password",
    textFieldType: TextFieldType = TextFieldType.REGULAR,
    showStrengthIndicator: Boolean = false,
    evaluateStrength: (String) -> PasswordStrength = { PasswordStrength.NONE }
) {
    var password by remember(initialText) { mutableStateOf(initialText) }
    var isVisible by remember { mutableStateOf(initiallyVisible) }
    var passwordStrength by remember(password) {
        mutableStateOf(evaluateStrength(initialText))
    }

    Box(modifier = modifier) {
        ValidatedTextField(
            modifier = Modifier.fillMaxWidth(),
            onInputChange = { newPassword, valid ->
                password = newPassword
                passwordStrength = evaluateStrength(newPassword)
                onPasswordChange(newPassword, valid)
            },
            label = label,
            initialText = initialText,
            isValid = isValid,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            enabled = enabled,
            readOnly = readOnly,
            textFieldType = textFieldType
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            if (showToggleVisibilityButton) {
                ToggleVisibilityButton(onVisibilityChanged = { isVisible = it })
            }
            if (showCopyToClipboardButton) {
                CopyToClipboardButton(
                    onClick = {
                        onCopyClicked(password)
                    }
                )
            }
        }
        Column (
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 4.dp)
        ) {
            if (showStrengthIndicator) {
                PasswordStrengthIndicator(strength = passwordStrength)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    PasswordField(
        showStrengthIndicator = true,
        textFieldType = TextFieldType.OUTLINED
    )
}