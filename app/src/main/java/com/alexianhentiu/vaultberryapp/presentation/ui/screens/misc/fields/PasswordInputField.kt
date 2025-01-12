package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
    onPasswordChange: (String, Boolean) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        TextField(
            value = password,
            onValueChange = {
                password = it
                isValid = it.isNotBlank()
                onPasswordChange(it, isValid)
            },
            label = { Text("Password") },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = !isValid,
            modifier = Modifier.fillMaxWidth()
        )
        ToggleVisibilityButton(
            onVisibilityChanged = { isVisible = it },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}