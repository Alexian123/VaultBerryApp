package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.fields

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun ValidatedInputField(
    modifier: Modifier = Modifier,
    onInputChange: (String, Boolean) -> Unit,
    label: String = "",
    initialText: String = "",
    isValid: (String) -> Boolean = { true },
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    var text by remember { mutableStateOf(initialText) }
    var isTextValid by remember { mutableStateOf(isValid(text)) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            isTextValid = isValid(it)
            onInputChange(it, isTextValid)
        },
        label = { Text(label) },
        isError = !isTextValid,
        modifier = modifier,
        visualTransformation = visualTransformation
    )
}