package com.alexianhentiu.vaultberryapp.presentation.ui.components.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.domain.utils.InputValidator
import com.alexianhentiu.vaultberryapp.presentation.ui.components.fields.ValidatedTextField

@Composable
fun Verify2FAForm(
    onContinueClicked: (String) -> Unit,
    onCancelClicked: () -> Unit,
    inputValidator: InputValidator
) {
    var code by remember { mutableStateOf("") }
    var isCodeValid by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Two-factor authentication",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )
        ValidatedTextField(
            label = "Code",
            onInputChange = { newCode, valid ->
                code = newCode
                isCodeValid = valid
            },
            isValid = inputValidator::validateEmail,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onContinueClicked(code) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isCodeValid
        ) {
            Text("Continue")
        }
        TextButton(
            onClick = { onCancelClicked() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Cancel")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Verify2FAFormPreview() {
    Verify2FAForm(
        onContinueClicked = {},
        onCancelClicked = {},
        inputValidator = InputValidator()
    )
}