package com.alexianhentiu.vaultberryapp.presentation.ui.screens.misc.dialogs

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ConfirmActionDialog(
    title: String,
    message: String,
    confirmButtonText: String = "Confirm",
    onDismissRequest: () -> Unit,
    onSubmit: (Boolean) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
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
                Row {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(12.dp),
                        onClick = onDismissRequest
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(12.dp),
                        onClick = { onSubmit(true) }
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
        onDismissRequest = {},
        onSubmit = {}
    )
}