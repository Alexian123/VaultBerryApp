package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.fields

import androidx.compose.foundation.layout.Box
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
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.ToggleVisibilityButton

@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    onPasswordChange: (String, Boolean) -> Unit,
    isValid: (String) -> Boolean = { true }
) {
    var isVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        ValidatedInputField(
            modifier = Modifier.fillMaxWidth(),
            onInputChange = onPasswordChange,
            label = "Password",
            isValid = isValid,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
        )
        ToggleVisibilityButton(
            onVisibilityChanged = { isVisible = it },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}