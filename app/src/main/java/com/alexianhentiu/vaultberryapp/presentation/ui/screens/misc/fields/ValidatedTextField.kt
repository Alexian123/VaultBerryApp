package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.buttons.CopyToClipboardButton

@Composable
fun ValidatedTextField(
    modifier: Modifier = Modifier,
    onInputChange: (String, Boolean) -> Unit,
    label: String = "",
    initialText: String = "",
    isValid: (String) -> Boolean = { true },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    showCopyToClipboardButton: Boolean = false,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
    var input by remember { mutableStateOf(initialText) }
    var isInputValid by remember { mutableStateOf(isValid(input)) }

    Box(modifier = modifier) {
        TextField(
            value = input,
            onValueChange = {
                input = it
                isInputValid = isValid(it)
                onInputChange(it, isInputValid)
            },
            label = { Text(label) },
            isError = !isInputValid,
            modifier = modifier,
            visualTransformation = visualTransformation,
            enabled = enabled,
            readOnly = readOnly
        )
        if (showCopyToClipboardButton) {
            CopyToClipboardButton(
                textToCopy = input,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ValidatedTextFieldPreview() {
    ValidatedTextField(
        onInputChange = { _, _ -> },
    )
}