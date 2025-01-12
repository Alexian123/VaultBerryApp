package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.CopyToClipboardButton
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.ToggleVisibilityButton

@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    onPasswordChange: (String, Boolean) -> Unit,
    initialText: String = "",
    isValid: (String) -> Boolean = { true },
    showCopyToClipboardButton: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ValidatedTextField(
            modifier = Modifier.fillMaxWidth(),
            onInputChange = { newPassword, valid ->
                password = newPassword
                onPasswordChange(newPassword, valid)
            },
            label = "Password",
            initialText = initialText,
            isValid = isValid,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            enabled = enabled,
            readOnly = readOnly
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            ToggleVisibilityButton(onVisibilityChanged = { isVisible = it })
            if (showCopyToClipboardButton) {
                CopyToClipboardButton(textToCopy = password)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PasswordFieldPreview() {
    PasswordField(
        onPasswordChange = { _, _ -> },
    )
}