package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.alexianhentiu.vaultberryapp.R

@Composable
fun ErrorDialog(
    onConfirm: () -> Unit,
    title: String = stringResource(R.string.error_dialog_title_default),
    message: String = stringResource(R.string.error_dialog_message_default)
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(R.string.error_dialog_confirm_button_text))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(onConfirm = {})
}