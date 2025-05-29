package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexianhentiu.vaultberryapp.R

@Composable
fun ErrorDialog(
    onConfirm: () -> Unit,
    title: String = stringResource(R.string.error_dialog_title_default),
    source: String = stringResource(R.string.app_name),
    message: String = stringResource(R.string.error_dialog_message_default),
    onSendReport: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(title) },
        text = {
            Column {
                Text(
                    text = "Source: $source",
                    fontWeight = FontWeight.Bold
                )
                Text(message)

            }
        },
        confirmButton = {
            Row {
                TextButton(
                    onClick = onSendReport,
                    content = {
                        Text(
                            text = stringResource(R.string.error_dialog_send_report_text),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onConfirm) {
                    Text(stringResource(R.string.error_dialog_confirm_button_text))
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorDialog() {
    ErrorDialog(
        onConfirm = {},
        onSendReport = {}
    )
}