package com.alexianhentiu.vaultberryapp.presentation.ui.components.dialogs.animated

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingAnimationDialog(
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingAnimationDialogPreview() {
    LoadingAnimationDialog()
}