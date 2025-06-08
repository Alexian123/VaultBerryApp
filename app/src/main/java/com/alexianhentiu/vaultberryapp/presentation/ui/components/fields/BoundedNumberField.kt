package com.alexianhentiu.vaultberryapp.presentation.ui.components.fields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.presentation.ui.theme.VaultBerryAppTheme

/**
 * A Composable OutlinedTextField that accepts only numbers within a specified range.
 *
 * @param value The current value of the field.
 * @param onValueChange Callback that is triggered when the value changes.
 *                      The Int passed is guaranteed to be within the [lowerBound] and [upperBound].
 * @param label The label to be displayed inside or above the text field.
 * @param lowerBound The minimum acceptable value (inclusive).
 * @param upperBound The maximum acceptable value (inclusive).
 * @param modifier Modifier for this text field.
 * @param enabled Controls the enabled state of the text field.
 * @param readOnly Controls the read-only state of the text field.
 * @param textStyle The text style to be applied to the input text.
 * @param singleLine When set to true, this text field becomes a single horizontally scrolling text field.
 * @param isError Indicates if the current value is in an error state.
 */
@Composable
fun BoundedNumberField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    lowerBound: Int,
    upperBound: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    if (lowerBound > upperBound) {
        throw IllegalArgumentException(
            "Lower bound ($lowerBound) cannot be greater than upper bound ($upperBound)."
        )
    }

    var textFieldValue by remember(value) { mutableStateOf(value.toString()) }
    var internalError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun validateAndClamp() {
        val currentNum = textFieldValue.toIntOrNull()
        if (currentNum != null) {
            val coerced = currentNum.coerceIn(lowerBound, upperBound)
            textFieldValue = coerced.toString()
            if (coerced != value) {
                onValueChange(coerced)
            }
            internalError = false
        } else {
            // Input is not a valid number
            textFieldValue = lowerBound.toString()
            if (lowerBound != value) {
                onValueChange(lowerBound)
            }
            internalError = false
        }
    }

    // Update text field if the external value changes and is different from current value.
    LaunchedEffect(value) {
        val currentTextFieldAsInt = textFieldValue.toIntOrNull()
        if (currentTextFieldAsInt != value || (textFieldValue != value.toString()) ) {
            textFieldValue = value.toString()
            if (value.toString().toIntOrNull() != null) {
                internalError = !(value in lowerBound..upperBound
                        && !textFieldValue.endsWith("-"))
            }
        }
    }

    val customKeyboardActions = KeyboardActions(
        onDone = {
            validateAndClamp()
            focusManager.clearFocus()
        }
    )

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newText ->
            textFieldValue = newText
            val number = newText.toIntOrNull()
            if (number != null) {
                internalError = number !in lowerBound..upperBound
                if (!internalError && number != value) {
                    onValueChange(number)
                }
            } else if (newText.isNotEmpty() && newText != "-") {
                internalError = true
            } else {
                internalError = false
            }
        },
        label = { Text(label) },
        modifier = modifier.onFocusChanged { focusState ->
            if (!focusState.isFocused) {
                validateAndClamp()
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = singleLine,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        isError = isError || internalError,
        keyboardActions = customKeyboardActions
    )
}

@Preview(showBackground = true)
@Composable
fun BoundedNumberFieldPreview() {
    var number by remember { mutableIntStateOf(50) }
    VaultBerryAppTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
        ) {
            Text("Enter a number between 0 and 100:")
            BoundedNumberField(
                value = number,
                onValueChange = { number = it },
                label = "Count",
                lowerBound = 0,
                upperBound = 100
            )
            Text("Current value: $number")
        }
    }
}