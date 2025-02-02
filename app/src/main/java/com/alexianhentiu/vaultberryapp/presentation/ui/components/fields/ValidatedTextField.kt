package com.alexianhentiu.vaultberryapp.presentation.ui.components.fields

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
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
import com.alexianhentiu.vaultberryapp.presentation.ui.components.buttons.CopyToClipboardButton
import com.alexianhentiu.vaultberryapp.presentation.ui.components.enums.TextFieldType

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
    readOnly: Boolean = false,
    textFieldType: TextFieldType = TextFieldType.REGULAR
) {
    var input by remember { mutableStateOf(initialText) }
    var isInputValid by remember { mutableStateOf(isValid(input)) }

    Box(modifier = modifier) {
        when (textFieldType) {
            TextFieldType.REGULAR ->
                TextField(
                    value = input,
                    onValueChange = {
                        input = it
                        isInputValid = isValid(it)
                        onInputChange(it, isInputValid)
                    },
                    label = { Text(label) },
                    isError = !isInputValid,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = visualTransformation,
                    enabled = enabled,
                    readOnly = readOnly
                )
            TextFieldType.OUTLINED ->
                OutlinedTextField(
                    value = input,
                    onValueChange = {
                        input = it
                        isInputValid = isValid(it)
                        onInputChange(it, isInputValid)
                    },
                    label = { Text(label) },
                    isError = !isInputValid,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = visualTransformation,
                    enabled = enabled,
                    readOnly = readOnly
                )
        }

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
        initialText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        label = "Text Input",
        showCopyToClipboardButton = true
    )
}