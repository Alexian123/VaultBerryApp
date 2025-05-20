package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.enums.PasswordStrength
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.PasswordField
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.CheckboxOptionRow
import com.alexianhentiu.vaultberryapp.presentation.utils.enums.TextFieldType
import com.alexianhentiu.vaultberryapp.presentation.utils.store.PasswordGenOptions
import java.lang.NumberFormatException

@Composable
fun PasswordGeneratorForm(
    modifier: Modifier = Modifier,
    initialOptions: PasswordGenOptions = PasswordGenOptions(),
    generatePassword: (PasswordGenOptions) -> String,
    evaluateStrength: (String) -> PasswordStrength,
    onPasswordCopy: (String) -> Unit
) {
    var options by remember { mutableStateOf(initialOptions) }
    var password by remember { mutableStateOf("") }

    fun generate() {
        password = if (options.length > 0) { // Basic validation
            generatePassword(options)
        } else {
            "" // Clear if length is invalid
        }
    }

    // Generate password initially and whenever options change
    LaunchedEffect(options) {
        generate()
    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (password.isNotEmpty()) {
            PasswordField(
                label = "",
                initialText = password,
                readOnly = true,
                initiallyVisible = true,
                showToggleVisibilityButton = false,
                showCopyToClipboardButton = true,
                onCopyClicked = onPasswordCopy,
                evaluateStrength = evaluateStrength,
                showStrengthIndicator = true,
                textFieldType = TextFieldType.OUTLINED
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Length Control
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Length:", modifier = Modifier.width(80.dp))
            OutlinedTextField(
                value = options.length.toString(),
                onValueChange = { value ->
                    val newLength = try {
                        value.toInt().coerceIn(8, 128)
                    } catch (_: NumberFormatException) {
                        if (value.isEmpty()) 0 else options.length // Allow empty for typing, else keep old
                    }
                    if (newLength != 0 || value.isEmpty()) { // Update if valid or if user is clearing the field
                        options = options.copy(length = newLength)
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(70.dp),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
            )
            Slider(
                value = options.length.toFloat(),
                onValueChange = {
                    options = options.copy(length = it.toInt())
                },
                valueRange = 8f..128f, // Adjust range as needed
                steps = 119, // valueRange.end - valueRange.start - 1
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox Options
        CheckboxOptionRow(
            text = "Include Uppercase (A-Z)",
            checked = options.includeUppercase,
            onCheckedChange = { options = options.copy(includeUppercase = it) }
        )
        CheckboxOptionRow(
            text = "Include Numbers (0-9)",
            checked = options.includeNumbers,
            onCheckedChange = { options = options.copy(includeNumbers = it) }
        )
        CheckboxOptionRow(
            text = "Include Special Chars (!@#...)",
            checked = options.includeSpecialChars,
            onCheckedChange = { options = options.copy(includeSpecialChars = it) }
        )
        CheckboxOptionRow(
            text = "Include Spaces",
            checked = options.includeSpaces,
            onCheckedChange = { options = options.copy(includeSpaces = it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { generate() }) { // Explicit generate button
            Text("Generate")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PasswordGeneratorFormPreview() {
    PasswordGeneratorForm(
        generatePassword = { _ -> "Password123!" },
        onPasswordCopy = {},
        evaluateStrength = { PasswordStrength.AVERAGE }
    )
}