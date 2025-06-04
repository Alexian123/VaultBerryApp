package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.alexianhentiu.vaultberryapp.R
import com.alexianhentiu.vaultberryapp.presentation.ui.components.misc.CheckboxOptionRow

@Composable
fun ConfirmActionDialog(
    title: String,
    message: String,
    showConfirmationCheckbox: Boolean = false,
    checkboxText: String = stringResource(R.string.confirmation_checkbox_text),
    confirmButtonText: String = stringResource(R.string.confirm_button_text),
    onDismissRequest: () -> Unit,
    onSubmit: (Boolean) -> Unit
) {
    var confirmationCheckboxChecked by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(message)
                Spacer(modifier = Modifier.height(16.dp))
                if (showConfirmationCheckbox) {
                    CheckboxOptionRow(
                        text = checkboxText,
                        checked = confirmationCheckboxChecked,
                        onCheckedChange = {
                            confirmationCheckboxChecked = it
                        }
                    )
                }
                Row {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(6.dp),
                        onClick = onDismissRequest
                    ) {
                        Text(stringResource(R.string.cancel_button_text))
                    }
                    Button(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(6.dp),
                        onClick = { onSubmit(true) },
                        enabled = confirmationCheckboxChecked || !showConfirmationCheckbox
                    ) {
                        Text(confirmButtonText)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmActionDialogPreview() {
    ConfirmActionDialog(
        title = "Confirm Action",
        message = "Are you sure you want to perform this action?",
        showConfirmationCheckbox = true,
        onDismissRequest = {},
        onSubmit = {}
    )
}